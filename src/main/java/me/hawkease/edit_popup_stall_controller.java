package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class edit_popup_stall_controller {

    @FXML private TextField shopNameField;
    @FXML private TextField shopAddressField;
    @FXML private ComboBox<String> categoryComboBox;

    private Stage stage;
    private edit_stalls parentController; // Reference to the main controller
    private double lat,lon;

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll("Food", "Cloth", "Electronics", "Home", "Beauty");
        categoryComboBox.setValue("Food");
    }

    public void set_co_ordinate(double lat,double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public void setParentController(edit_stalls parentController) {
        this.parentController = parentController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleSubmit() {
        String shopName = shopNameField.getText();
        String shopAddress = shopAddressField.getText();
        String category = categoryComboBox.getValue();

        if (parentController != null) {
            parentController.updateStallInfo(shopName, shopAddress, category);
        }

        stage.close();
    }

    public void load_data(){
        shop_keepers_sql sql = new shop_keepers_sql();
        info_for_stall info = sql.get_stall_info(lat,lon);
        shopNameField.setText(info.name());
        shopAddressField.setText(info.address());
        categoryComboBox.setValue(info.type());
    }
}
