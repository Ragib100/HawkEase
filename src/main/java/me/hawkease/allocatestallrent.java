package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;

public class allocatestallrent {

    @FXML
    public BorderPane page;

    @FXML
    private Text massage;

    @FXML
    private TextField rent_amount;

    private double selectedLat = -180;
    private double selectedLon = -180;
    private ArrayList<LocationInfo> locations = new ArrayList<>();

    @FXML
    void openmap(MouseEvent event) {
        mapviewer mapViewer = new mapviewer(this);
        location_sql sql = new location_sql();
        locations = sql.getLocations();
        locations.sort(new Comparator<LocationInfo>() {
            @Override
            public int compare(LocationInfo l1, LocationInfo l2) {
                int latCompare = Double.compare(l1.getLatitude(), l2.getLatitude());
                if (latCompare != 0) return latCompare;
                return Double.compare(l1.getLongitude(), l2.getLongitude());
            }
        });
        mapViewer.setExistingLocations(locations);
        mapViewer.show();
    }

    @FXML
    void allocate_rent(MouseEvent event) {
        System.out.println(selectedLat + " " + selectedLon);
        location_sql loc = new location_sql();
        loc.insert_location(selectedLat,selectedLon,rent_amount.getText());
        locations.add(new LocationInfo(selectedLat, selectedLon, rent_amount.getText()));
    }

    public void setLocation(double lat, double lon) {
        this.selectedLat = lat;
        this.selectedLon = lon;
        this.massage.setText(String.format("%.6f, %.6f", lat, lon));
        // Here you can add any additional logic you want to perform with the location
        // For example, storing it in a database, calculating distances, etc.
    }

    public BorderPane getPage() {
        return page;
    }

}