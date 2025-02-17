package me.hawkease;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class pendingReq {

    @FXML
    void doUpdate(MouseEvent event) {

    }

    @FXML
    private TableColumn<NameStatus, String> nameColumn;

    @FXML
    private Pagination pagination;

    @FXML
    private TableColumn<NameStatus, ToggleGroup> statusColumn;

    @FXML
    private Button actionButton;

    @FXML
    private TableView<NameStatus> tableView;

    private static final int rows = 20;
    private final ObservableList<NameStatus> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        // Set a custom cell factory for the status column
        statusColumn.setCellFactory(column -> new TableCell<NameStatus, ToggleGroup>() {
            @Override
            protected void updateItem(ToggleGroup toggleGroup, boolean empty) {
                super.updateItem(toggleGroup, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Create radio buttons for "Accepted" and "Rejected"
                    RadioButton acceptedButton = new RadioButton("Accepted");
                    RadioButton rejectedButton = new RadioButton("Rejected");

                    // Group the radio buttons
                    ToggleGroup group = new ToggleGroup();
                    acceptedButton.setToggleGroup(group);
                    rejectedButton.setToggleGroup(group);

                    // Bind the selected status to the NameStatus object
                    NameStatus currentItem = getTableView().getItems().get(getIndex());
                    group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue == acceptedButton) {
                            currentItem.setStatus("Accepted");
                        } else if (newValue == rejectedButton) {
                            currentItem.setStatus("Rejected");
                        }
                    });

                    // Set the initial state based on the current status
                    if ("Accepted".equals(currentItem.getStatus())) {
                        acceptedButton.setSelected(true);
                    } else if ("Rejected".equals(currentItem.getStatus())) {
                        rejectedButton.setSelected(true);
                    }

                    // Add the radio buttons to an HBox
                    HBox hBox = new HBox(10, acceptedButton, rejectedButton);
                    setGraphic(hBox);
                }
            }
        });

        // Populate the table with sample data
        loadData();

        // Set the data to the table
        tableView.setItems(data);

        // Handle the submit button action
        actionButton.setOnAction(event -> handleSubmit());
    }

    private void loadData() {
        // Add sample data to the ObservableList
        for (int i = 1; i <= 50; i++) {
            data.add(new NameStatus("User " + i, ""));
        }
    }

    private void handleSubmit() {
        // Print the status of each user
        for (NameStatus item : data) {
            System.out.println("User: " + item.getUserName() + ", Status: " + item.getStatus());
        }
    }

    // Inner class to represent the data model
    public static class NameStatus {
        private final SimpleStringProperty userName;
        private String status;

        public NameStatus(String userName, String status) {
            this.userName = new SimpleStringProperty(userName);
            this.status = status;
        }

        public String getUserName() {
            return userName.get();
        }

        public void setUserName(String userName) {
            this.userName.set(userName);
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}