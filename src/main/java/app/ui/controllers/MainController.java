package app.ui.controllers;

import app.domain.Asset;
import app.domain.AssetStatus;
import app.io.AssetFileImporter;
import app.repository.AssetRepository;
import app.repository.InMemoryAssetRepository;
import app.service.AssetManager;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MainController {


    // fxml refs
    @FXML private TableView<Asset> tblAssets;
    @FXML private TableColumn<Asset, String> colName, colTag, colStatus, colAcquired;
    @FXML private TableColumn<Asset, Integer> colQty;
    @FXML private TableColumn<Asset, BigDecimal> colUnitCost;
    @FXML private TextField txtSearchTag;
    @FXML private ComboBox<String> cmbStatus;
    @FXML private Label lblTotalValue, lblStatus;
    @FXML public TableColumn<Asset, String> colLocation;


    private final AssetRepository repo = new InMemoryAssetRepository();
    private final AssetManager manager = new AssetManager(repo);
    private final ObservableList<Asset> master = FXCollections.observableArrayList();
    private final ObservableList<Asset> filtered = FXCollections.observableArrayList();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @FXML
    public void initialize() {
        // table bindings (adapt getters if yours differ)
        colName.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getName())));
        colTag.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getAssetTag())));
        colLocation.setCellValueFactory(c -> Bindings.createObjectBinding(() -> nz(c.getValue().getLocation())));
        colStatus.setCellValueFactory(c -> Bindings.createObjectBinding(() -> c.getValue().getStatus() == null ? "" : c.getValue().getStatus().name()));
        colQty.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getQuantity));
        colUnitCost.setCellValueFactory(c -> Bindings.createObjectBinding(c.getValue()::getUnitCost));
        colUnitCost.setCellFactory(col -> new TableCell<Asset, BigDecimal>() {
            private final java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance();
            @Override protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null
                        ? ""
                        : fmt.format(value.setScale(2, java.math.RoundingMode.HALF_UP)));
            }
        });
        colAcquired.setCellValueFactory(c -> Bindings.createObjectBinding(() -> {
            try {
                var d = c.getValue().getPurchaseDate();
                return d == null ? "" : dateFmt.format(d);
            } catch (Exception e) { return ""; }
        }));

        // filters
        cmbStatus.getItems().add(0, "All Statuses");
        cmbStatus.getItems().addAll("IN_STOCK", "ACTIVE", "IN_REPAIR", "RETIRED");
        cmbStatus.getSelectionModel().selectFirst();

        // load data
        master.setAll(manager.listAll());
        filtered.setAll(master);
        tblAssets.setItems(filtered);


        // totals + status
        updateTotalFrom(filtered);
        setStatus("Ready");
    }

    // filters
    @FXML
    private void onFilterChanged() {
        String tag = nz(txtSearchTag.getText()).toLowerCase();
        String statusSel = cmbStatus.getSelectionModel().getSelectedItem();

        List<Asset> items = master.stream()
                .filter(a -> tag.isBlank() || nz(a.getAssetTag()).toLowerCase().contains(tag))
                .filter(a -> "All Statuses".equals(statusSel) || (a.getStatus() != null && a.getStatus().name().equals(statusSel)))
                .collect(Collectors.toList());

        filtered.setAll(items);
        updateTotalFrom(filtered);
        setStatus("Filtered " + filtered.size() + "/" + master.size());
    }

    // menu/buttons (wire real dialogs next step)



    @FXML private void onAbout()       { setStatus("Invexus DMS v1.0 — CEN-3024C"); }
    @FXML private void onExit()        { tblAssets.getScene().getWindow().hide(); }

    // helpers
    private void updateTotalFrom(List<Asset> list) {
        // include only IN_STOCK, ASSIGNED, IN_REPAIR
        BigDecimal total = list.stream()
                .filter(a -> a.getStatus() != null)
                .filter(a -> switch (a.getStatus()) {
                    case IN_STOCK, ACTIVE, IN_REPAIR -> true;
                    default -> false; // RETIRED excluded
                })
                .map(a -> a.getUnitCost().multiply(BigDecimal.valueOf(a.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, java.math.RoundingMode.HALF_UP);

        // currency format
        var fmt = java.text.NumberFormat.getCurrencyInstance();
        lblTotalValue.setText(fmt.format(total));
        tblAssets.refresh();
    }

    private static String nz(String s) { return s == null ? "" : s.trim(); }
    private void setStatus(String s) { if (lblStatus != null) lblStatus.setText(s); }

    @FXML
    private void onAddAsset() {
        Asset newAsset = AssetFormController.showDialog(null);
        if (newAsset == null) {
            setStatus("Add canceled");
            return;
        }

        boolean ok = manager.add(newAsset);
        if (ok) {
            master.add(newAsset);
            tblAssets.refresh();
            onFilterChanged(); // refreshes filtered list and total
            setStatus("Added asset " + newAsset.getAssetTag());
        } else {
            setStatus("Add failed (duplicate tag?)");
        }
    }


    @FXML private void onEditAsset() {
        Asset selected = tblAssets.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select a row to edit");
            return;
        }
        tblAssets.refresh();
        Asset edited = AssetFormController.showDialog(selected);
        if (edited != null) {
            boolean ok = manager.update(edited);
            if (ok) {
                int index = master.indexOf(selected);
                master.set(index, edited);
                tblAssets.refresh();
                onFilterChanged();
                setStatus("Edited asset " + edited.getAssetTag());
            } else {
                setStatus("update failed. (Correct ID tag?)");
            }
        }else{
            setStatus("Edit canceled");
        }


    }

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
        } else {
            setStatus("Delete failed");
        }
    }

    @FXML
    private boolean showConfirm(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES,ButtonType.CANCEL);
        alert.setHeaderText(null);
        alert.setTitle("Confirm");
        var result= alert.showAndWait().orElse(ButtonType.CANCEL);
        return result == ButtonType.YES;
    }

    @FXML
    private void onImportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Asset File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Assets Files", "*.csv", "*.txt"));

        File file = fileChooser.showOpenDialog(tblAssets.getScene().getWindow());
        if (file == null) return;

        try {
            AssetFileImporter importer = new AssetFileImporter();
            List<Asset> parsed = importer.parseCsv(file.getAbsolutePath());

            if (parsed.isEmpty()) {
                setStatus("No valid assets found in file.");
                return;
            }

            int imported = manager.importAssets(parsed);
            master.setAll(manager.listAll());
            onFilterChanged();
            setStatus("Imported " + imported + " of " + parsed.size() + " assets.");
        } catch (Exception e) {
            setStatus("Import failed: " + e.getMessage());
        }

    }


}

