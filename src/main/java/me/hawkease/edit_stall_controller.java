package me.hawkease;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

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