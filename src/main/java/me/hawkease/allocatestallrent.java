package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class allocatestallrent implements map_controller {

    @FXML
    public BorderPane page;

    @FXML
    private Text massage;

    @FXML
    private TextField rent_amount;

    private double selectedLat = -180;
    private double selectedLon = -180;
    HashMap<location_info, Boolean> loc;
    mapviewer mapViewer;

    @FXML
    void open_map(MouseEvent event) {
        try {
            loc = new HashMap<>();
            mapViewer = new mapviewer(this);
            locations_sql sql = new locations_sql();
            ArrayList<location_info> locations = sql.getLocations();
            for (location_info Location : locations) {
                loc.put(new location_info(Location.getLatitude(), Location.getLongitude()),Boolean.TRUE);
            }
            shop_keepers_sql sql1 = new shop_keepers_sql();
            locations = sql1.getLocations();
            for (location_info location : locations) {
                loc.put(new location_info(location.getLatitude(), location.getLongitude()),Boolean.FALSE);
            }
            mapViewer.setExistingLocations(loc);
            mapViewer.show();
        } catch (Exception e) {
            make_alert.getInstance().make_alert("Error","Map loading failed");
        }
    }

    @FXML
    void delete_stall(MouseEvent event) {
        try{
            location_info checkLocation = new location_info(selectedLat, selectedLon);
            if (!loc.containsKey(checkLocation) || loc.get(checkLocation) == null || !loc.get(checkLocation)) {
                massage.setText("Location Can not be deleted");
            }
            else {
                locations_sql sql = new locations_sql();
                if(sql.delete_location(selectedLat, selectedLon)){
                    massage.setText("Location has been deleted");
                    loc.remove(checkLocation);
                }
                else massage.setText("Location deletion failed");
            }
        }
        catch (Exception e) {
            massage.setText("Error deleting location: ");
        }
    }

    @FXML
    void allocate_rent(MouseEvent event) {
        try {
            System.out.println(selectedLat + " " + selectedLon);

            if (selectedLat == -180 || selectedLon == -180) {
                massage.setText("Please select a location first");
                return;
            }

            if (rent_amount.getText() == null || rent_amount.getText().trim().isEmpty()) {
                massage.setText("Please enter a rent amount");
                return;
            }

            try {
                Double.parseDouble(rent_amount.getText());
            } catch (NumberFormatException e) {
                massage.setText("Rent amount must be a valid number");
                return;
            }

            locations_sql loc_sql = new locations_sql();
            if(loc_sql.check_location(selectedLat, selectedLon) && loc_sql.update_location(selectedLat,selectedLon,rent_amount.getText())) massage.setText("Rent updated successfully");
            else if(loc_sql.insert_location(selectedLat, selectedLon, rent_amount.getText())){
                massage.setText("New stall allocated successfully");
                loc.put(new location_info(selectedLat, selectedLon),true);
            }
            else massage.setText("Operation failed");
        } catch (Exception e) {
            massage.setText("Error in allocating stall");
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