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
            location_sql sql = new location_sql();
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
    void requestt(MouseEvent event) {
        System.out.println(selectedLat + " " + selectedLon);
        try {
            // Validate input
            System.out.println(selectedLat + " " + selectedLon);
            if (selectedLat == -180 || selectedLon == -180) {
                if (locationText != null) {
                    locationText.setText("Please select a location first");
                }
                return;
            }

            location_info checkLocation = new location_info(selectedLat, selectedLon);
            if (loc.containsKey(checkLocation) && !loc.get(checkLocation)) {
                System.out.println("Can not be requested");
                return;
            }
            else {
                System.out.println("done");
            }

            if (locationText != null) {
                location_sql sql = new location_sql();
                if(!sql.check_location(selectedLat,selectedLon)) {
                    System.out.println("Invalid location");
                    return;
                }

                requests_sql mysql = new requests_sql();
                mysql.request_location(selectedLat, selectedLon);
            }

            selectedLat = -180;
            selectedLon = -180;

        } catch (Exception e) {
            System.err.println("Error requesting location: "+e.getMessage());
            e.printStackTrace();
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
            System.out.println(lat+","+lon+","+selectedLat+","+selectedLon);
            System.out.println("Error setting location: ");
        }
    }

    @Override
    public BorderPane getPage() {
        return page;
    }

    void sort_locations(){
        if (locations != null) {
            System.out.println("Locations found");
            locations.sort((l1, l2) -> {
                if (l1 == null && l2 == null) return 0;
                if (l1 == null) return -1;
                if (l2 == null) return 1;

                int latCompare = Double.compare(l1.getLatitude(), l2.getLatitude());
                if (latCompare != 0) return latCompare;
                return Double.compare(l1.getLongitude(), l2.getLongitude());
            });
        }
    }
}