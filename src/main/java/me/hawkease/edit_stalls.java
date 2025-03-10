package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class edit_stalls implements map_controller{

    @FXML
    public BorderPane page;

    @FXML
    public Text massage;

    HashMap<location_info,Boolean>loc;
    mapviewer mapViewer;

    double latitude,longitude;

    @FXML
    void edit_the_current_stall(MouseEvent event) {
        // open pop up editing page for stall
    }

    @FXML
    void open_map(MouseEvent event) {
        try {
            loc = new HashMap<>();
            mapViewer = new mapviewer(this);

            shop_keepers_sql sql1 = new shop_keepers_sql();
            ArrayList<location_info> locations = sql1.getLocations(current_user.get_user().get_email());

            for (location_info location : locations) {
                loc.put(new location_info(location.getLatitude(), location.getLongitude()),Boolean.FALSE);
            }
            mapViewer.setExistingLocations(loc);
            mapViewer.show();
        } catch (Exception e) {
            System.err.println("Error opening map: " + e.getMessage());
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
