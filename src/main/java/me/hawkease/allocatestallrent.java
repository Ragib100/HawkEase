package me.hawkease;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Comparator;

public class allocatestallrent implements map_controller {

    @FXML
    public BorderPane page;

    @FXML
    private Text massage;

    @FXML
    private TextField rent_amount;

    private double selectedLat = -180;
    private double selectedLon = -180;
    private ArrayList<location_info> locations = new ArrayList<>();

    @FXML
    void open_map(MouseEvent event) {
        try {
            mapviewer mapViewer = new mapviewer(this);
            location_sql sql = new location_sql();
            locations = sql.getLocations();

            if (locations != null) {
                locations.sort(new Comparator<location_info>() {
                    @Override
                    public int compare(location_info l1, location_info l2) {
                        if (l1 == null && l2 == null) return 0;
                        if (l1 == null) return -1;
                        if (l2 == null) return 1;

                        int latCompare = Double.compare(l1.getLatitude(), l2.getLatitude());
                        if (latCompare != 0) return latCompare;
                        return Double.compare(l1.getLongitude(), l2.getLongitude());
                    }
                });
            }

            mapViewer.setExistingLocations(locations);
            mapViewer.show();
        } catch (Exception e) {
            System.err.println("Error opening map: " + e.getMessage());
        }
    }

    @FXML
    void delete_stall(MouseEvent event) {
        try{
            location_sql sql = new location_sql();
            sql.delete_location(selectedLat, selectedLon);
//            System.out.println("Deleted location: " + selectedLat + ", " + selectedLon);
        }
        catch (Exception e) {
            System.err.println("Error deleting stall");
        }
    }

    @FXML
    void allocate_rent(MouseEvent event) {
        try {
            System.out.println(selectedLat + " " + selectedLon);

            // Validate input
            if (selectedLat == -180 || selectedLon == -180) {
                massage.setText("Please select a location first");
                return;
            }

            if (rent_amount.getText() == null || rent_amount.getText().trim().isEmpty()) {
                massage.setText("Please enter a rent amount");
                return;
            }

            // Try to parse rent amount as a number
            try {
                Double.parseDouble(rent_amount.getText());
            } catch (NumberFormatException e) {
                massage.setText("Rent amount must be a valid number");
                return;
            }

            location_sql loc = new location_sql();
            if(loc.check_location(selectedLat, selectedLon)) loc.update_location(selectedLat,selectedLon,rent_amount.getText());
            else loc.insert_location(selectedLat, selectedLon, rent_amount.getText());
        } catch (Exception e) {
            System.err.println("Error allocating rent: " + e.getMessage());
            massage.setText("Error: " + e.getMessage());
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