package me.hawkease;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class edit_stalls implements map_controller {

    @FXML
    public BorderPane page;

    @FXML
    public Text massage;

    HashMap<location_info, Boolean> loc;
    mapviewer mapViewer;
    double latitude = -180, longitude = -180;

    @FXML
    void edit_the_current_stall(MouseEvent event) {
        try {
            if (latitude == -180 || longitude == -180) {
                massage.setText("Choose a location");
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit_stall_popup.fxml"));
            Parent root = fxmlLoader.load();

            // Get controller instance
            edit_popup_stall_controller popupController = fxmlLoader.getController();

            // Set parent reference
            popupController.setParentController(this);

            // Create a popup window
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Stall");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.DECORATED);

            popupStage.setScene(new Scene(root));

            // Set stage reference inside popup controller
            popupController.setStage(popupStage);

            popupStage.showAndWait();
        } catch (IOException e) {
            make_alert.getInstance().make_alert("Error", "Try again");
        }
    }

    // Method to receive data from popup
    public void updateStallInfo(String shopName, String shopAddress, String category) {
        shop_keepers_sql sql = new shop_keepers_sql();
        if(sql.update(latitude, longitude, category, shopName, shopAddress)) make_alert.getInstance().make_alert("Success", "Stall information updated successfully");
        else make_alert.getInstance().make_alert("Error", "Try again");
    }

    @FXML
    void open_map(MouseEvent event) {
        try {
            loc = new HashMap<>();
            mapViewer = new mapviewer(this);

            shop_keepers_sql sql1 = new shop_keepers_sql();
            ArrayList<location_info> locations = sql1.getLocations(current_user.get_user().get_email());

            for (location_info location : locations) {
                loc.put(new location_info(location.getLatitude(), location.getLongitude()), Boolean.FALSE);
            }
            mapViewer.setExistingLocations(loc);
            mapViewer.show();
        } catch (Exception e) {
            make_alert.getInstance().make_alert("Error", "Try again");
        }
    }

    @Override
    public void setLocation(double lat, double lon) {
        latitude = lat;
        longitude = lon;
    }

    @Override
    public BorderPane getPage() {
        return page;
    }
}
