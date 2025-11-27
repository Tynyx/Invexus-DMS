package app.ui.controllers;

import app.config.DB;
import app.domain.Asset;
import app.io.AssetFileImporter;
import app.repository.ARepoMySQL;
import app.repository.AssetRepository;
import app.service.AssetManager;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the main Invexus DMS window, handling table display,
 * filtering, and asset CRUD actions.
 */
public final class MainController {


    /**
     * Creates a new main controller instance.
     * <p>
     * This no-argument constructor is required by the JavaFX FXML loader.
     */
    public MainController() {
        // no-op
    }


    private final AssetRepository repo = new ARepoMySQL(DB.dataSource());
    private final AssetManager manager = new AssetManager(repo);



    // fxml refs
    @FXML private TableView<Asset> tblAssets;
    @FXML private TableColumn<Asset, String> colName, colTag, colStatus, colAcquired;
    @FXML private TableColumn<Asset, Integer> colQty;
    @FXML private TableColumn<Asset, BigDecimal> colUnitCost;
    @FXML private TextField txtSearchTag;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblTotalValue, lblStatus;

    /**
     * Table column used to display the location of each asset.
     */
    @FXML public TableColumn<Asset, String> colLocation;


    private final ObservableList<Asset> master = FXCollections.observableArrayList();
    private final ObservableList<Asset> filtered = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    /**
     * JavaFX lifecycle hook. Initializes table columns, loads data from the repository,
     * and applies default filters and totals.
     */

    @FXML
    public void initialize() {

        // table bindings
        colName.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getName())));
        colTag.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getAssetTag())));
        colLocation.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getLocation())));
        colStatus.setCellValueFactory(c -> Bindings.createObjectBinding(() -> c.getValue().getStatus() == null ? "" : c.getValue().getStatus().name()));
        colStatus.setCellFactory(col -> new TableCell<Asset, String>() {
            @Override protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null || value.isBlank()) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label pill = new Label(value);
                pill.getStyleClass().addAll("pill", "pill-" + value); // e.g., pill-IN_STOCK
                setGraphic(pill);
                setText(null);
            }
        });
        colQty.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getQuantity));
        colUnitCost.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getUnitCost));
        colUnitCost.setCellFactory(_ -> new TableCell<Asset, BigDecimal>() {
            private final NumberFormat fmt = NumberFormat.getCurrencyInstance();
            @Override protected void updateItem(BigDecimal v, boolean empty) {
                super.updateItem(v, empty);
                setText(empty || v == null ? "" : fmt.format(v.setScale(2, RoundingMode.HALF_UP)));
            }
        });

        lblTotalValue.setText(java.text.NumberFormat.getCurrencyInstance()
                .format(java.math.BigDecimal.ZERO.setScale(2)));


        colAcquired.setCellValueFactory(c -> Bindings.createObjectBinding(() -> {
            try {
                var d = c.getValue().getPurchaseDate();
                return d == null ? "" : dateFmt.format(d);
            } catch (Exception e) { return ""; }
        }));

        // filters

        cmbStatus.getItems().setAll("All Statuses", "IN_STOCK", "ASSIGNED", "REPAIR", "RETIRED");
        cmbStatus.getSelectionModel().selectFirst();

        // load data
        master.setAll(manager.listAll());
        filtered.setAll(master);

        // totals + status
        tblAssets.setItems(filtered);
        updateTotalFrom(filtered);
        setStatus("Ready");
    }


    /** Re-query DB and re-apply the current filters. */
    private void refreshTable() {
        Asset sel = tblAssets.getSelectionModel().getSelectedItem();
        var latest = manager.listAll();
        master.setAll(latest);   // or findAll()
        onFilterChanged();                  // reapply filter + totals
        if (sel != null) {
            master.stream()
                    .filter(a -> a.getAssetTag().equals(sel.getAssetTag()))
                    .findFirst()
                    .ifPresent(a -> tblAssets.getSelectionModel().select(a));
        }
        var late = manager.listAll();
        master.setAll(latest);
        onFilterChanged();
        System.out.println("DB rows after refresh: " + late.size());

    }

    /**
     * Display an error message
     * @param header usually the top of the popup window with the word 'Error'
     * @param message display what caused the error or the given error message
     */
    private void showError(String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage owner = (Stage) tblAssets.getScene().getWindow();
            if (owner != null) alert.initOwner(owner);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(message == null || message.isBlank() ? "(no details)" : message);
            alert.showAndWait();
        });
    }





    // filters

    /**
     * Applies current tag/status filters, updates table rows and total value.
     * refresh the table upon add, edit, delete and ImportCSV.
     */
    @FXML
    private void onFilterChanged() {
        String tag = nz(txtSearchTag.getText()).toLowerCase();
        String statusSel = cmbStatus.getSelectionModel().getSelectedItem();

        List<Asset> items = master.stream()
                .filter(a -> tag.isBlank() || nz(a.getAssetTag()).toLowerCase().contains(tag))
                .filter(a -> "All Statuses".equals(statusSel)
                        || (a.getStatus() != null && a.getStatus().name().equals(statusSel)))
                .collect(Collectors.toList());

        filtered.setAll(items);
        updateTotalFrom(filtered);
        setStatus("Filtered " + filtered.size() + "/" + master.size());
        System.out.println("JDBC URL = " + DB.dataSource());
    }


    // menu/buttons (wire real dialogs next step)


    /**
     *  Shows and "About" message in the status line.
     */
    @FXML private void onAbout()       { setStatus("Invexus DMS v1.0 — CEN-3024C"); refreshTable(); }


    /**
     * Exits the application window
     */
    @FXML private void onExit()        { tblAssets.getScene().getWindow().hide(); refreshTable(); }

    // helpers

    private void updateTotalFrom(List<Asset> list) {
        BigDecimal total = list.stream()
                .filter(a -> a.getStatus() != null)
                .filter(a -> switch (a.getStatus()) {
                    case IN_STOCK, ASSIGNED, REPAIR -> true;
                    default -> false;
                })
                .map(a -> a.getUnitCost().multiply(BigDecimal.valueOf(a.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        lblTotalValue.setText(NumberFormat.getCurrencyInstance().format(total));
    }


    private static String nz(String s) { return s == null ? "" : s.trim();  }
    private void setStatus(String s) { if (lblStatus != null) lblStatus.setText(s); }

    // After actions:


    /**
     * Deletes a selected asset.
     */
    @FXML
    private void onDeleteAsset() {
        Asset selected = tblAssets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select a row to delete");
            return;
        }

        boolean confirm = showConfirm("Delete asset" +  selected.getAssetTag() + "?");
        if (!confirm) {
            setStatus("Delete canceled");
            return;
        }

        boolean ok = manager.delete(selected.getAssetTag());
        if (ok) {
            master.remove(selected);
            onFilterChanged();
            setStatus("Deleted: " + selected.getAssetTag());
            refreshTable();
        } else {
            setStatus("Delete failed");
        }

    }

    /**
     * display a message for confirming an asset that is being edited or deleted
     * @param msg the string that contains a message about the selected asset
     * @return if user clicks yes returns the message "Confirm"
     */
    @FXML
    private boolean showConfirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES,ButtonType.CANCEL);
        alert.setHeaderText(null);
        alert.setTitle("Confirm");
        var result= alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.YES;
    }


    /**
     * Handles CSV/TXT import flow, validates duplicate tags within the file,
     * persists rows via the manager, then refreshes the table and totals.
     */
    @FXML
    private void onImportCSV() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Select Asset File");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Assets Files", "*.csv", "*.txt"));
        File file = fc.showOpenDialog(tblAssets.getScene().getWindow());
        if (file == null) return;

        try {
            AssetFileImporter importer = new AssetFileImporter();
            List<Asset> parsed = importer.parseCsv(file.getAbsolutePath());
            if (parsed.isEmpty()) { setStatus("No valid assets found."); return; }

            // Guard: duplicates inside the file itself
            var seen = new java.util.HashSet<String>();
            var dupes = parsed.stream()
                    .map(Asset::getAssetTag)
                    .filter(t -> !seen.add(t))
                    .distinct()
                    .toList();
            if (!dupes.isEmpty()) {
                showError("Import failed",
                        "Duplicate tags found in file: " + String.join(", ", dupes));
                return;
            }

            // Save to repo; DO NOT addAll(parsed) to master
            int imported = manager.importAssets(parsed);

            refreshTable();
            setStatus("Imported " + imported + " of " + parsed.size() + " assets.");
        } catch (Exception e) {
            showError("Import failed", e.getMessage());
        }

    }


    /**
     * Opens the add/edit dialog for a given asset.
     * @param editing asset to edit; {@code null} opens the dialog in "Add" mode
     */
    private void showAssetDialog(Asset editing) {
        try {
            FXMLLoader fx = new FXMLLoader(getClass().getResource("/ui/asset_form.fxml"));

            // Build controller with deps + callback
            AssetFormController controller =
                    new AssetFormController(manager, editing, saved -> {
                        if (Boolean.TRUE.equals(saved)) {
                            refreshTable();     // requery DB + refresh table
                            setStatus(editing == null ? "Asset added." : "Asset updated.");
                        } else {
                            setStatus("Add/Edit canceled");
                        }
                    });

            fx.setController(controller);
            Parent root = fx.load();

            Stage owner = (Stage) tblAssets.getScene().getWindow();
            Stage dlg = new Stage();
            dlg.initOwner(owner);
            dlg.initModality(javafx.stage.Modality.WINDOW_MODAL);
            dlg.setTitle(editing == null ? "Add Asset" : "Edit Asset");
            dlg.setScene(new Scene(root));
            dlg.showAndWait();     // blocks until dialog closes
        } catch (Exception ex) {
            showError("Dialog error", ex.getMessage());
        }
    }


    /**
     * Display a message when an asset is added
     */
    @FXML private void onAddAsset()  { showAssetDialog(null); }

    /**
     * display a message when choosing to edit an asset.
     */
    @FXML private void onEditAsset() {
        Asset sel = tblAssets.getSelectionModel().getSelectedItem();
        if (sel == null) { setStatus("Select a row to edit"); return; }
        showAssetDialog(sel);
    }


}

