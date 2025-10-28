package app.ui.controllers;

import app.domain.Asset;
import app.domain.AssetStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AssetFormController {

    @FXML private DatePicker txtAcquired;
    @FXML private TextField txtLocation;
    @FXML private TextField txtTag;
    @FXML private TextField txtName;
    @FXML private TextField txtQty;
    @FXML private TextField txtUnitCost;
    @FXML private ComboBox<AssetStatus> cmbStatus;


    private Asset working;
    private boolean saved = false;

    @FXML
    public void initialize() {
        cmbStatus.getItems().setAll(AssetStatus.values());
    }

    public void setAsset(Asset a) {
        this.working = a == null ? null : a.copy();

        if (a != null) {
            txtTag.setText(a.getAssetTag());
            txtName.setText(a.getName());
            txtLocation.setText(a.getLocation());
            txtUnitCost.setText(a.getUnitCost().toPlainString());
            txtQty.setText(String.valueOf(a.getQuantity()));
            txtAcquired.setValue(a.getPurchaseDate());
            cmbStatus.setValue(a.getStatus());
           
        } else {
            cmbStatus.getSelectionModel().selectFirst();
        }
    }




    @FXML private void onCancel() {
        close(false);
    }

    @FXML private void onSave() {
        try {
            String name = txtName.getText().trim();
            String tag = txtTag.getText().trim();
            String location = txtLocation.getText().trim();
            BigDecimal unit = new BigDecimal(txtUnitCost.getText().trim());
            int qty = Integer.parseInt(txtQty.getText().trim());


            LocalDate date = txtAcquired.getValue();
            Asset newAsset = new Asset(
                    tag,
                    name,
                    location,
                    date,
                    unit,
                    qty,
                    cmbStatus.getValue(),
                    false

            );

            this.working = newAsset;


            close(true);
        } catch (Exception e) {
            e.printStackTrace(); // or you can add error popups later
        }
    }

    private void close(boolean saved) {
        this.saved = saved;
        ((Stage) txtName.getScene().getWindow()).close();
    }




    public static Asset showDialog(Asset existing) {
        try {
            FXMLLoader loader = new FXMLLoader(AssetFormController.class.getResource("/ui/asset_form.fxml"));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(existing == null ? "Add Asset" : "Edit Asset");
            stage.setScene(new Scene(loader.load()));
            AssetFormController c = loader.getController();
            c.setAsset(existing);
            stage.showAndWait();
            return c.saved ? c.working : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
