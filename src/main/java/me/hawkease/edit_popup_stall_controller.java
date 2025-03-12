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
    @FXML private Button submitButton;

    private Stage stage;
    private edit_stalls parentController; // Reference to the main controller

    @FXML
    public void initialize() {
        categoryComboBox.getItems().addAll("Food", "Cloth", "Electronics", "Home", "Beauty");
        categoryComboBox.setValue("Food");
    }

    // Method to set the parent controller (edit_stalls)
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

        // Pass data to edit_stalls controller
        if (parentController != null) {
            parentController.updateStallInfo(shopName, shopAddress, category);
        }

        // Close the popup
        stage.close();
    }
}
