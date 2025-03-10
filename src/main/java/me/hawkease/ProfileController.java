package me.hawkease;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    // Enum for different user types
    public enum UserType {
        ADMIN, SELLER, BUYER
    }

    private UserType currentUserType = UserType.BUYER; // Default type
    private File selectedImageFile;

    @FXML private BorderPane mainLayout;
    @FXML private ImageView profileImageView;
    @FXML private Label userTypeLabel;
    @FXML private Button selectImageButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Personal information fields
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    // Password fields
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    // User specific pane to be populated dynamically
    @FXML private TitledPane specificInfoPane;

    // Fields for the dynamic content based on user type
    private GridPane specificGrid;

    // Admin specific fields
    private TextField departmentField;
    private TextField roleField;

    // Seller specific fields
    private TextField shopNameField;
    private ComboBox<String> businessTypeCombo;
    private TextField licenseField;

    // Buyer specific fields
    private CheckBox foodCheck;
    private CheckBox clothingCheck;
    private CheckBox electronicsCheck;
    private CheckBox craftsCheck;
    private ComboBox<String> notificationsCombo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up default profile image
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream("/images/default_profile.png"));
            profileImageView.setImage(defaultImage);
        } catch (Exception e) {
            System.out.println("Could not load default profile image: " + e.getMessage());
        }

        // Update UI based on user type
        updateUserTypeSpecificUI();

        // Load user profile data
        loadProfileData();
    }

    /**
     * Updates the user-specific part of the UI based on current user type
     */
    private void updateUserTypeSpecificUI() {
        // Update user type label
        userTypeLabel.setText("User Type: " + currentUserType.toString());

        // Create specific fields grid
        specificGrid = new GridPane();
        specificGrid.setVgap(10);
        specificGrid.setHgap(10);
        specificGrid.setPadding(new Insets(20));

        // Reset the specific pane content
        specificInfoPane.setContent(specificGrid);

        switch (currentUserType) {
            case ADMIN:
                createAdminSpecificFields();
                break;

            case SELLER:
                createSellerSpecificFields();
                break;

            case BUYER:
                createBuyerSpecificFields();
                break;
        }
    }

    /**
     * Creates admin-specific fields
     */
    private void createAdminSpecificFields() {
        specificInfoPane.setText("Admin Information");

        Label departmentLabel = new Label("Department:");
        departmentField = new TextField();
        departmentField.setPromptText("Enter department");

        Label roleLabel = new Label("Role:");
        roleField = new TextField();
        roleField.setPromptText("Enter role");

        specificGrid.add(departmentLabel, 0, 0);
        specificGrid.add(departmentField, 1, 0);
        specificGrid.add(roleLabel, 0, 1);
        specificGrid.add(roleField, 1, 1);
    }

    /**
     * Creates seller-specific fields
     */
    private void createSellerSpecificFields() {
        specificInfoPane.setText("Seller Information");

        Label shopNameLabel = new Label("Shop Name:");
        shopNameField = new TextField();
        shopNameField.setPromptText("Enter shop name");

        Label businessTypeLabel = new Label("Business Type:");
        businessTypeCombo = new ComboBox<>();
        businessTypeCombo.getItems().addAll("Food", "Clothing", "Electronics", "Crafts", "Other");

        Label licenseLabel = new Label("License Number:");
        licenseField = new TextField();
        licenseField.setPromptText("Enter license number");

        specificGrid.add(shopNameLabel, 0, 0);
        specificGrid.add(shopNameField, 1, 0);
        specificGrid.add(businessTypeLabel, 0, 1);
        specificGrid.add(businessTypeCombo, 1, 1);
        specificGrid.add(licenseLabel, 0, 2);
        specificGrid.add(licenseField, 1, 2);
    }

    /**
     * Creates buyer-specific fields
     */
    private void createBuyerSpecificFields() {
        specificInfoPane.setText("Buyer Preferences");

        Label preferredCategoriesLabel = new Label("Preferred Categories:");

        VBox categoriesBox = new VBox(5);
        foodCheck = new CheckBox("Food");
        clothingCheck = new CheckBox("Clothing");
        electronicsCheck = new CheckBox("Electronics");
        craftsCheck = new CheckBox("Crafts");
        categoriesBox.getChildren().addAll(foodCheck, clothingCheck, electronicsCheck, craftsCheck);

        Label notificationsLabel = new Label("Notifications:");
        notificationsCombo = new ComboBox<>();
        notificationsCombo.getItems().addAll("All", "Important Only", "None");
        notificationsCombo.setValue("All");

        specificGrid.add(preferredCategoriesLabel, 0, 0);
        specificGrid.add(categoriesBox, 1, 0);
        specificGrid.add(notificationsLabel, 0, 1);
        specificGrid.add(notificationsCombo, 1, 1);
    }

    /**
     * Loads profile data from database or other storage
     */
    private void loadProfileData() {
        // TODO: This would connect to your database or storage to fetch user profile data
        // For demo purposes, we'll just set some sample data

        // Set common fields
        nameField.setText("John Doe");
        emailField.setText("john.doe@example.com");
        phoneField.setText("+1 (555) 123-4567");
        addressField.setText("123 Main Street, City");

        // Set user-specific fields based on type
        switch (currentUserType) {
            case ADMIN:
                departmentField.setText("IT Department");
                roleField.setText("System Administrator");
                break;

            case SELLER:
                shopNameField.setText("John's Food Corner");
                businessTypeCombo.setValue("Food");
                licenseField.setText("LIC-12345-FOOD");
                break;

            case BUYER:
                foodCheck.setSelected(true);
                electronicsCheck.setSelected(true);
                notificationsCombo.setValue("Important Only");
                break;
        }
    }

    /**
     * Handle profile image selection
     */
    @FXML
    private void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        selectedImageFile = fileChooser.showOpenDialog(mainLayout.getScene().getWindow());

        if (selectedImageFile != null) {
            try {
                Image selectedImage = new Image(selectedImageFile.toURI().toString());
                profileImageView.setImage(selectedImage);
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load the selected image.");
            }
        }
    }

    /**
     * Handle saving profile changes
     */
    @FXML
    private void handleSave(ActionEvent event) {
        // Validation
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Name and email are required fields.");
            return;
        }

        // Password validation
        if (!newPasswordField.getText().isEmpty()) {
            if (currentPasswordField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter your current password.");
                return;
            }

            if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Error", "New password and confirmation do not match.");
                return;
            }

            // Verify the current password
            if (!verifyCurrentPassword(currentPasswordField.getText())) {
                showAlert(Alert.AlertType.ERROR, "Error", "Current password is incorrect.");
                return;
            }
        }

        // User type specific validation
        switch (currentUserType) {
            case SELLER:
                if (shopNameField.getText().isEmpty() || businessTypeCombo.getValue() == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Shop name and business type are required for sellers.");
                    return;
                }
                break;
        }

        // Save profile data
        saveProfileData();

        showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
    }

    /**
     * Handle cancel button action
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        // Get the stage and close it
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Verify the current password against stored password
     * @param password The current password to verify
     * @return True if password is correct, false otherwise
     */
    private boolean verifyCurrentPassword(String password) {
        // TODO: This would connect to your authentication system
        // For demo purposes, always return true
        return true;
    }

    /**
     * Save profile data to storage
     */
    private void saveProfileData() {
        // TODO: This method would save the data to your database
        // For this example, we'll just print the data
        System.out.println("Saving profile data for user type: " + currentUserType);
        System.out.println("Name: " + nameField.getText());
        System.out.println("Email: " + emailField.getText());
        System.out.println("Phone: " + phoneField.getText());
        System.out.println("Address: " + addressField.getText());

        if (selectedImageFile != null) {
            System.out.println("New profile image: " + selectedImageFile.getAbsolutePath());
        }

        // Save user type specific data
        switch (currentUserType) {
            case ADMIN:
                System.out.println("Department: " + departmentField.getText());
                System.out.println("Role: " + roleField.getText());
                break;

            case SELLER:
                System.out.println("Shop Name: " + shopNameField.getText());
                System.out.println("Business Type: " + businessTypeCombo.getValue());
                System.out.println("License Number: " + licenseField.getText());
                break;

            case BUYER:
                System.out.println("Preferred Categories:");
                System.out.println("- Food: " + foodCheck.isSelected());
                System.out.println("- Clothing: " + clothingCheck.isSelected());
                System.out.println("- Electronics: " + electronicsCheck.isSelected());
                System.out.println("- Crafts: " + craftsCheck.isSelected());
                System.out.println("Notifications: " + notificationsCombo.getValue());
                break;
        }
    }

    /**
     * Show an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Set the user type and update the UI accordingly
     * @param userType The user type to set
     */
    public void setUserType(UserType userType) {
        this.currentUserType = userType;
        updateUserTypeSpecificUI();
        loadProfileData();
    }
}