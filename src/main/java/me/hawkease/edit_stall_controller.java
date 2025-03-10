package me.hawkease;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class edit_stall_controller implements map_controller {

    @FXML
    private Text massage;

    @FXML
    private BorderPane page;

    HashMap<location_info, Boolean> loc;
    mapviewer mapViewer;
    private double selectedLat = -180;
    private double selectedLon = -180;

    @FXML
void edit_stall_info(MouseEvent event) {
    try {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/edit_stall_popup.fxml"));
        Parent root = loader.load();
        
        // Get the controller
        edit_popup_stall_controller controller = loader.getController();
        
        // Create a new stage
        Stage popupStage = new Stage();
        popupStage.setTitle("Edit Stall Information");
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(root));
        
        // Set the stage in the controller
        controller.setStage(popupStage);
        
        // Show the stage and wait for it to close
        popupStage.showAndWait();
        
        // After the popup is closed, get the values if needed
        if (controller.getShopName() != null) {
            String shopName = controller.getShopName();
            String shopAddress = controller.getShopAddress();
            String category = controller.getCategory();
            
            // Process the data (e.g., save to database)
            System.out.println("Shop Name: " + shopName);
            System.out.println("Shop Address: " + shopAddress);
            System.out.println("Category: " + category);
        }
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    void open_map(MouseEvent event) {
        try {
            loc = new HashMap<>();
            mapViewer = new mapviewer(this);
            shop_keepers_sql sql = new shop_keepers_sql();
            ArrayList<location_info> locations = sql.getLocations(current_user.get_user().get_email());
            for (location_info Location : locations) {
                loc.put(new location_info(Location.getLatitude(), Location.getLongitude()),Boolean.FALSE);
            }
            mapViewer.setExistingLocations(loc);
            mapViewer.show();
        } catch (Exception e) {
            System.err.println("Error opening map: " + e.getMessage());
        }
    }

    @Override
    public void setLocation(double lat, double lon) {
        this.selectedLat = lat;
        this.selectedLon = lon;
        this.massage.setText(String.format("%.6f, %.6f", lat, lon));
    }

    @Override
    public BorderPane getPage() {
        return page;
    }

}