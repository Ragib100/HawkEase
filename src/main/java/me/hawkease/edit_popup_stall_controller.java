package me.hawkease; // Update with your actual package name

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
    
    // For storing the result
    private String shopName;
    private String shopAddress;
    private String category;
    
    @FXML
    public void initialize() {
        // Initialize the combo box with options
        categoryComboBox.getItems().addAll("Food", "Cloth", "Electronics", "Home", "Beauty");
        categoryComboBox.setValue("Food"); // Default value
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    void handleSubmit() {
        // Store the values
        shopName = shopNameField.getText();
        shopAddress = shopAddressField.getText();
        category = categoryComboBox.getValue();
        
        // Close the window
        stage.close();
    }
    
    // Getters for the form values
    public String getShopName() {
        return shopName;
    }
    
    public String getShopAddress() {
        return shopAddress;
    }
    
    public String getCategory() {
        return category;
    }
}