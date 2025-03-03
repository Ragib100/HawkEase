package me.hawkease;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class pendingReq implements Initializable {

    @FXML
    private TableColumn<EmailRequest, String> nameColumn;

    @FXML
    private TableColumn<EmailRequest, String> statusColumn;

    @FXML
    private TableView<EmailRequest> tableView;

    // Observable list to hold email requests
    private final ObservableList<EmailRequest> pendingRequests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure name column to display email
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Configure status column to display buttons
        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");
            private final Button rejectButton = new Button("Reject");
            private final HBox buttonContainer = new HBox(10); // 10 is the spacing between buttons

            {
                // Style the buttons
                acceptButton.getStyleClass().add("accept-button");
                acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                rejectButton.getStyleClass().add("reject-button");
                rejectButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                // Set button actions
                acceptButton.setOnAction(event -> handleAccept(getIndex()));
                rejectButton.setOnAction(event -> handleReject(getIndex()));

                // Configure container
                buttonContainer.setAlignment(Pos.CENTER);
                buttonContainer.getChildren().addAll(acceptButton, rejectButton);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonContainer);
                }
            }
        });

        // Load sample data
        loadSampleData();

        // Set the items to the TableView
        tableView.setItems(pendingRequests);
    }

    // Handle accept button click
    private void handleAccept(int index) {
        if (index >= 0 && index < pendingRequests.size()) {
            EmailRequest request = pendingRequests.get(index);
            String email = request.getEmail();
            System.out.println("Accepted request from: " + email);
            requests_sql sql = new requests_sql();

            pendingRequests.remove(index);
        }
    }

    // Handle reject button click
    private void handleReject(int index) {
        if (index >= 0 && index < pendingRequests.size()) {
            EmailRequest request = pendingRequests.get(index);
            System.out.println("Rejected request from: " + request.getEmail());

            // Here you would typically call your service to process the rejection
            // For example: userService.rejectRequest(request.getEmail());

            // Remove the request from the table
            pendingRequests.remove(index);
        }
    }

    // Load sample data for testing
    private void loadSampleData() {
        pendingRequests.add(new EmailRequest("john.doe@example.com"));
        pendingRequests.add(new EmailRequest("jane.smith@example.com"));
        pendingRequests.add(new EmailRequest("michael.johnson@example.com"));
        pendingRequests.add(new EmailRequest("sarah.williams@example.com"));
        pendingRequests.add(new EmailRequest("robert.brown@example.com"));
    }

    // Model class for email requests
    public static class EmailRequest {
        private final SimpleStringProperty email;

        public EmailRequest(String email) {
            this.email = new SimpleStringProperty(email);
        }

        public String getEmail() {
            return email.get();
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

    }
}