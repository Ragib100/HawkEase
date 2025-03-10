package me.hawkease;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class shop_keepers_sql {
    private Connection con;

    public shop_keepers_sql() {
        try{
            con = database_connection.get_connection().sql_connection();
        }
        catch (Exception e) {
            System.out.println("SQL connection could not be established!");
        }
    }

    public boolean insert(String email,double lat,double lon) {
        String query = "INSERT INTO shop_keepers(email,latitude,longitude) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Acception successful");
                return true;
            }
            System.out.println("Acception failed");
        }
        catch (Exception e) {
            System.out.println("SQL connection could not be established!");
        }
        return false;
    }

    public ArrayList<location_info> getLocations() {
        ArrayList<location_info> locations = new ArrayList<>();

        String query = "SELECT latitude, longitude FROM shop_keepers";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                locations.add(new location_info(latitude, longitude));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving locations: " + e.getMessage());
        }

        return locations;
    }

    public ArrayList<location_info> getLocations(String email) {
        ArrayList<location_info> locations = new ArrayList<>();

        String query = "SELECT latitude, longitude FROM shop_keepers WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                locations.add(new location_info(latitude, longitude));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving locations: " + e.getMessage());
        }

        return locations;
    }

    public boolean is_under_3(String email) {
        String query = "SELECT COUNT(email) AS count FROM shop_keepers WHERE email = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.println("Checking under 3: " + count);
                return count < 3;
            }
        }
        catch (Exception e) {
            System.out.println("Error checking email count: " + e.getMessage());
        }
        return false;
    }
}
