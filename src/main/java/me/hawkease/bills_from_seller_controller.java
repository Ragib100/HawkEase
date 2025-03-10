package me.hawkease;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class bills_from_seller_controller implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ListView<location_info> locationListView;

    @FXML
    private ImageView qrCodeImage;

    private ObservableList<location_info> location_infoList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Create sample location data
        location_infoList = FXCollections.observableArrayList(getSampleLocationData());

        // Set up the location list view
        setupLocationListView();

        // Ensure the main BorderPane and ListView can resize properly
        mainBorderPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        locationListView.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    }

    private void setupLocationListView() {
        // Set the items in the ListView
        locationListView.setItems(location_infoList);

        // Ensure the ListView can resize properly
        locationListView.setMinHeight(100);
        locationListView.setMinWidth(200);

        // Define a custom cell factory to show location info and Pay Bill button with center alignment
        locationListView.setCellFactory(listView -> new ListCell<location_info>() {
            private final HBox container = new HBox();
            private final TextField locationTextField = new TextField();
            private final Button payButton = new Button("Pay Bill");

            {
                // Set up the container with center alignment
                container.setSpacing(10);
                container.setPadding(new Insets(5, 10, 5, 10));
                container.setAlignment(Pos.CENTER); // Center align the content
                container.setMinWidth(200); // Set minimum width

                // Configure the location text field
                locationTextField.setEditable(false);
                locationTextField.setPrefWidth(250);
                locationTextField.setMinWidth(150); // Set minimum width
                locationTextField.setAlignment(Pos.CENTER); // Center text in the field

                // Set up the pay button
                payButton.setOnAction(event -> {
                    location_info location = getItem();
                    if (location != null) {
                        showPaymentPopup(location);
                    }
                });
                payButton.setMinWidth(70); // Ensure button doesn't disappear when resizing

                // Add a spacer to push elements to the center
                HBox.setHgrow(locationTextField, Priority.ALWAYS);

                // Add components to the container
                container.getChildren().addAll(locationTextField, payButton);
            }

            @Override
            protected void updateItem(location_info location, boolean empty) {
                super.updateItem(location, empty);

                if (empty || location == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    locationTextField.setText(String.format("Latitude: %.6f, Longitude: %.6f",
                            location.getLatitude(), location.getLongitude()));
                    setGraphic(container);

                    // Ensure the cell itself is center aligned
                    setAlignment(Pos.CENTER);

                    // Make the cell respond to resize
                    setPrefWidth(container.getPrefWidth());
                }
            }
        });
    }

    private void showPaymentPopup(location_info location) {
        try {
            // Load the payment popup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment_pop_up.fxml"));
            Parent root = loader.load();

            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Payment for Location");
            popupStage.setMinWidth(300); // Set minimum size for popup
            popupStage.setMinHeight(200);

            // Find and update the location label
            Label locationLabel = (Label) root.lookup("#locationLabel");
            if (locationLabel != null) {
                locationLabel.setText(String.format("Location: %.6f, %.6f",
                        location.getLatitude(), location.getLongitude()));
            }

            // Find transaction ID field
            TextField transactionIdField = (TextField) root.lookup("#transactionIdField");

            // Set up the cancel button action
            Button cancelButton = (Button) root.lookup("#cancelButton");
            if (cancelButton != null) {
                cancelButton.setOnAction(e -> popupStage.close());
            }

            // Set up the submit button action
            Button submitButton = (Button) root.lookup("#submitButton");
            if (submitButton != null) {
                submitButton.setOnAction(e -> {
                    if (transactionIdField != null && !transactionIdField.getText().isEmpty()) {
                        processTransaction(location, transactionIdField.getText());
                        popupStage.close();
                    }
                });
            }

            // Set the scene and show the popup
            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTransaction(location_info location, String transactionId) {
        // Here you would implement the actual transaction processing
        System.out.println("Processing transaction: " + transactionId +
                " for location [" + location.getLatitude() + ", " +
                location.getLongitude() + "]");
        bills_from_seller_sql bills_from_seller_sql = new bills_from_seller_sql();
        if(bills_from_seller_sql.insert(current_user.get_user().get_email(), transactionId, location.getLatitude(), location.getLongitude())){
            System.out.println("Bill processed successfully");
        }
        else{
            System.out.println("Bill processing failed");
        }

        // Add validation, database updates, etc.
    }

    private ArrayList<location_info> getSampleLocationData() {
        shop_keepers_sql shop_keepers_sql = new shop_keepers_sql();
        return shop_keepers_sql.getLocations(current_user.get_user().get_email());
    }
}