package me.hawkease;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class pendingReq implements Initializable {

    ArrayList<location_requests> emailLocations;

    @FXML
    private TableColumn<location_requests, String> nameColumn;

    @FXML
    private TableColumn<location_requests, String> statusColumn;

    @FXML
    private TableView<location_requests> tableView;

    private final ObservableList<location_requests> pendingRequests = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().email()+","+cellData.getValue().lat()+" "+cellData.getValue().lon())
        );

        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");
            private final Button rejectButton = new Button("Reject");
            private final HBox buttonContainer = new HBox(10);

            {
                acceptButton.getStyleClass().add("accept-button");
                acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                rejectButton.getStyleClass().add("reject-button");
                rejectButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");

                acceptButton.setOnAction(event -> handleAccept(getIndex()));
                rejectButton.setOnAction(event -> handleReject(getIndex()));

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

        loadSampleData();

        tableView.setItems(pendingRequests);
    }

    private void loadSampleData() {
        request_sql sql = new request_sql();
        emailLocations = sql.get_requests();
        Collections.sort(emailLocations);
        pendingRequests.addAll(emailLocations);
    }

    private void handleAccept(int index) {
        if (index >= 0 && index < pendingRequests.size()) {
            location_requests request = pendingRequests.get(index);
            
            System.out.println("Accepted request from: " + request.email() + " (Lat: " + request.lat() + ", Lon: " + request.lon() + ")");
            
            shop_keepers_sql temp = new shop_keepers_sql();
            if(!temp.insert(request.email(), request.lat(), request.lon())) return;
            send_email send = new send_email();
            send.sendEmail(request.email(),"One of your requested locations is accepted");
            location_requests cur_loc = emailLocations.get(index);
            co_ordinate flag = new co_ordinate(cur_loc.lat(), cur_loc.lon());
            
            rent_to_be_paid_sql rent_to_be_paid_sql = new rent_to_be_paid_sql();
            rent_to_be_paid_sql.insert(request.email(), request.lat(), request.lon());
            
            int lb = lower_bound(flag);
            int ub = upper_bound(flag);
            System.out.println(lb + " " + ub);
            
            for(int i=ub-1; i>=lb; i--) {
                handleReject(i);
            }
        }
    }

    private void handleReject(int index) {
        if (index >= 0 && index < pendingRequests.size()) {
            location_requests request = pendingRequests.get(index);
            System.out.println("Rejected request from: " + request.email() + " (Lat: " + request.lat() + ", Lon: " + request.lon() + ")");
            
            request_sql sql = new request_sql();
            sql.delete_request(request.email(), request.lat(), request.lon());
            
            pendingRequests.remove(index);
            if (index < emailLocations.size()) {
                emailLocations.remove(index); 
            }
        }
    }

    int lower_bound(co_ordinate flag) {
        int l = 0, h = emailLocations.size()-1, res = emailLocations.size();
        while(l <= h) {
            int mid = l + ((h-l) >> 1);
            location_requests cur_loc = emailLocations.get(mid);
            co_ordinate cur = new co_ordinate(cur_loc.lat(), cur_loc.lon());
            if(cur.is_less_than(flag)) {
                l = mid + 1;
            }
            else {
                res = mid;
                h = mid - 1;
            }
        }
        return res;
    }

    int upper_bound(co_ordinate flag) {
        int l = 0, h = emailLocations.size()-1, res = emailLocations.size();
        while(l <= h) {
            int mid = l + ((h-l) >> 1);
            location_requests cur_loc = emailLocations.get(mid);
            co_ordinate cur = new co_ordinate(cur_loc.lat(), cur_loc.lon());
            if(cur.is_less_than(flag) || cur.is_equal(flag)) {
                l = mid + 1;
            }
            else {
                res = mid;
                h = mid - 1;
            }
        }
        return res;
    }
}