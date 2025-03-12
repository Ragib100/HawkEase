package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class requestforaplace implements map_controller {

    @FXML
    private BorderPane page;

    @FXML
    private Text locationText;

    private double selectedLat = -180;
    private double selectedLon = -180;

    ArrayList<location_info> locations;
    HashMap<location_info, Boolean> loc;

    @FXML
    void open_map(MouseEvent event) {
        try {
            loc = new HashMap<>();
            mapviewer mapViewer = new mapviewer(this);
            locations_sql sql = new locations_sql();
            locations = sql.getLocations();
            for (location_info location : locations) {
                loc.put(new location_info(location.getLatitude(), location.getLongitude()),true);
            }
            shop_keepers_sql sql2 = new shop_keepers_sql();
            locations = sql2.getLocations();
            for (location_info location : locations) {
                loc.put(new location_info(location.getLatitude(), location.getLongitude()),Boolean.FALSE);
            }
            mapViewer.setExistingLocations(loc);
            mapViewer.show();
        } catch (Exception e) {

            System.err.println("Error opening map: " + e.getMessage());
            if (locationText != null) {
                locationText.setText("Error opening map");
            }
        }
    }

    @FXML
    void request(MouseEvent event) {
        System.out.println(selectedLat + " " + selectedLon);
        try {
            locationText.setText("Selected location: " + selectedLat + " " + selectedLon);
            if (selectedLat == -180 || selectedLon == -180) {
                if (locationText != null) {
                    locationText.setText("Please select a location first");
                }
                return;
            }

            location_info checkLocation = new location_info(selectedLat, selectedLon);
            if (loc.containsKey(checkLocation) && !loc.get(checkLocation)) {
                locationText.setText("Can not be requested");
                return;
            }

            if (locationText != null) {
                locations_sql sql = new locations_sql();
                if(!sql.check_location(selectedLat,selectedLon)) {
                    locationText.setText("Invalid location");
                    return;
                }

                request_sql mysql = new request_sql();
                if(mysql.request_location(selectedLat, selectedLon)) locationText.setText("Requesting stall successful");
                else locationText.setText("Requesting stall failed");
            }

            selectedLat = -180;
            selectedLon = -180;

        } catch (Exception e) {
            make_alert.getInstance().make_alert("Error","Try again");
        }
    }

    @Override
    public void setLocation(double lat, double lon) {
        this.selectedLat = lat;
        this.selectedLon = lon;
        try{
            locationText.setText(String.format("Selected location: %.6f, %.6f", lat, lon));
        }
        catch(Exception e){
            make_alert.getInstance().make_alert("Error","Try again");
        }
    }

    @Override
    public BorderPane getPage() {
        return page;
    }

}