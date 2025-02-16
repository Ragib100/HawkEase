package me.hawkease;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.List;

public class pendingReq {
    @FXML private TableView<NameStatus> tableView;
    @FXML private TableColumn<NameStatus, String> nameColumn;
    @FXML private TableColumn<NameStatus, ToggleGroup> statusColumn;
    @FXML private Pagination pagination;
    @FXML private Button actionButton;

    private static final int ROWS_PER_PAGE = 20;
    private ObservableList<NameStatus> fullData = FXCollections.observableArrayList();

    public void initialize() {
        // Name Column Setup
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());

        // Status Column Setup with Radio Buttons
        statusColumn.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(ToggleGroup item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    NameStatus nameStatus = getTableView().getItems().get(getIndex());
                    HBox hBox = new HBox(10, nameStatus.getAcceptedRadio(), nameStatus.getRejectedRadio());
                    setGraphic(hBox);
                }
            }
        });

        // Load sample data (Replace with database query)
        fullData.addAll(loadDummyUsers(200)); // Simulating 200 users

        // Setup pagination
        pagination.setPageCount((int) Math.ceil((double) fullData.size() / ROWS_PER_PAGE));
        pagination.setPageFactory(this::createPage);
    }

    private List<NameStatus> loadDummyUsers(int count) {
        ObservableList<NameStatus> users = FXCollections.observableArrayList();
        for (int i = 1; i <= count; i++) {
            users.add(new NameStatus("User " + i));
        }
        return users;
    }

    private TableView<NameStatus> createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, fullData.size());

        tableView.setItems(FXCollections.observableArrayList(fullData.subList(fromIndex, toIndex)));
        return tableView;
    }

    @FXML
    private void doUpdate() {
        System.out.println("Update button clicked!");
        for (NameStatus user : tableView.getItems()) {
            String status = user.getAcceptedRadio().isSelected() ? "Accepted" : "Rejected";
            System.out.println(user.getName() + " - " + status);
            // TODO: Update status in the database
        }
    }
}