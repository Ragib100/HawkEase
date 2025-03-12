package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController extends sign_up_controller implements Initializable {

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextFlow confirm_password_massage;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private Text emailField;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextFlow new_password_massage;

    @FXML
    private TextField phoneField;

    @FXML
    private Circle profileImageBackground;

    @FXML
    private ImageView profileImageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize UI components
        new_password_massage.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16px");
        confirm_password_massage.setStyle("-fx-font-family: 'Times New Roman'; -fx-font-size: 16px");

        // Setup listeners for password fields using parent class methods
        newPasswordField.setOnKeyReleased(event -> {
            // Use the existing methods from parent class
            password_strength_message = new_password_massage;  // Redirect output to our TextFlow
            take_password = newPasswordField;  // Set the field to validate
            validate_password();  // Call parent's method
        });

        confirmPasswordField.setOnKeyReleased(event -> {
            // Use the existing methods from parent class
            password_same_message = confirm_password_massage;  // Redirect output to our TextFlow
            take_password = newPasswordField;  // Set the source password field
            take_password_again = confirmPasswordField;  // Set the confirmation field
            validate_repeated_password();  // Call parent's method
        });

        // Add focus listeners to clear messages when fields lose focus
        newPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // When focus is lost
                new_password_massage.getChildren().clear();
            }
        });

        confirmPasswordField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {  // When focus is lost
                confirm_password_massage.getChildren().clear();
            }
        });

        // Load user data
        loadUserData();
    }

    private void loadUserData() {
        // TODO: Load current user data from database
        try {
            user_sql userSql = new user_sql();
            info_for_profile info = userSql.get_user();
            emailField.setText(info.email());
            nameField.setText(info.name());
            phoneField.setText(info.number());

            // TODO: Load profile image if exists
        } catch (Exception e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }

    @FXML
    void handleSave(MouseEvent event) {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.isEmpty()) {

            boolean isCurrentPasswordValid = verifyCurrentPassword(currentPassword);

            if (!isCurrentPasswordValid) {
                showAlert("Error", "Current password is incorrect.");
                return;
            }

            if (!valid_password(newPassword)) {
                showAlert("Error", "New password does not meet the requirements.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showAlert("Error", "New passwords do not match.");
                return;
            }

            try{
                user_sql userSql = new user_sql();
                if(userSql.change_password(current_user.get_user().get_email(), newPassword,current_user.get_user().get_type())) showAlert("Success", "Password changed.");
                else showAlert("Error", "Passwords do not match.");
            } catch (Exception e) {
                System.out.println("Error changing password: ");
            }
        }

        try {
            user_sql userSql = new user_sql();
            if(userSql.update_user(name,phone)) showAlert("Success", "Profile updated successfully.");
            else showAlert("Error", "Profile update failed.");

            loadUserData();
        } catch (Exception e) {
            showAlert("Error", "Failed to update profile: " + e.getMessage());
        }
    }

    private boolean verifyCurrentPassword(String currentPassword) {
        try {
            user_sql userSql = new user_sql();
            return userSql.check_user(current_user.get_user().get_email(), currentPassword,current_user.get_user().get_type());
        } catch (Exception e) {
            System.out.println("Error verifying password: " + e.getMessage());
            return false;
        }
    }

    @FXML
    void handleSelectImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(mainLayout.getScene().getWindow());
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                profileImageView.setImage(image);

                // TODO: Save the image path to the database
            } catch (Exception e) {
                showAlert("Error", "Failed to load image: " + e.getMessage());
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}