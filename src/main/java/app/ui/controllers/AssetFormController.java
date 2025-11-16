package app.ui.controllers;

import app.domain.Asset;
import app.domain.AssetStatus;
import app.service.AssetManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * Controller for the asset add/edit modal. Validates user input and persists through
 * {@link app.service.AssetManager}.
 */
public class AssetFormController {
    private final AssetManager manager;
    private final Asset editing;                 // null => Add
    private final Consumer<Boolean> onClose;     // callback to parent

    // >>> constructor used by controllerFactory

    /**
     * Creates a controller for the asset form dialog.
     * @param manager the asset manager used to persist changes
     * @param editing the asset editing phase when editing
     * @param onClose the window used to display what happens on close
     */
    public AssetFormController(AssetManager manager,
                               Asset editing,
                               Consumer<Boolean> onClose) {
        this.manager  = manager;
        this.editing  = editing;
        this.onClose  = onClose;
    }

    // FXML fields
    @FXML private TextField txtTag, txtName, txtLocation, txtQty, txtUnitCost;
    @FXML private DatePicker txtAcquired;
    @FXML private ComboBox<AssetStatus> cmbStatus;
    @FXML private CheckBox chkAssigned;

    /**
     * Prefills form fields in edit mode and populates status choices.
     */
    @FXML
    private void initialize() {
        cmbStatus.getItems().setAll(AssetStatus.values());
        if (editing != null) {                   // prefill for edit
            txtTag.setText(editing.getAssetTag());
            txtName.setText(editing.getName());
            txtLocation.setText(editing.getLocation());
            txtQty.setText(String.valueOf(editing.getQuantity()));
            txtUnitCost.setText(editing.getUnitCost().toPlainString());
            txtAcquired.setValue(editing.getPurchaseDate());
            cmbStatus.setValue(editing.getStatus());
            chkAssigned.setSelected(editing.isAssigned());
            txtTag.setDisable(true);             // keep PK immutable
        } else {
            cmbStatus.getSelectionModel().selectFirst();
        }
    }

    /**
     * Validates and saves the asset form.
     */
    @FXML
    private void onSave() {
        try {
            var a = new Asset(
                    txtTag.getText().trim(),
                    txtName.getText().trim(),
                    txtLocation.getText().trim(),
                    txtAcquired.getValue(),
                    new BigDecimal(txtUnitCost.getText().trim()),
                    Integer.parseInt(txtQty.getText().trim()),
                    cmbStatus.getValue(),
                    chkAssigned.isSelected()
            );
            boolean ok = (editing == null) ? manager.add(a) : manager.update(a);
            close(ok);
        } catch (Exception ex) {
            close(false);
        }
    }

    /**
     * Cancel and action that was about to be done for add/edit on an asset
     */
    @FXML private void onCancel() { close(false); }

    private void close(boolean saved) {
        if (onClose != null) onClose.accept(saved);
        ((Stage) txtTag.getScene().getWindow()).close();
    }


}
