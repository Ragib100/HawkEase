package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class requestforaplace implements map_controller {

    @FXML
    private BorderPane page;

    @FXML
    private Text locationText;

    private double selectedLat = -180;
    private double selectedLon = -180;

    @FXML
    void open_map(MouseEvent event) {
        try {
            mapviewer mapViewer = new mapviewer(this);
            location_sql sql = new location_sql();
            ArrayList<location_info> locations = sql.getLocations();

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

            mapViewer.setExistingLocations(locations);
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
        try {
            // Validate input
            if (selectedLat == -180 || selectedLon == -180) {
                if (locationText != null) {
                    locationText.setText("Please select a location first");
                }
                return;
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
            System.err.println("Error requesting location: ");
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
}