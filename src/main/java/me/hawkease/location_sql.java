package me.hawkease;

import java.sql.*;
import java.util.ArrayList;

public class location_sql {
    private Connection con;

    public location_sql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ragib", "root", "asdf1234");
            con = DriverManager.getConnection("jdbc:mysql://ragib.mysql.database.azure.com:3306/root", "Ragib100", "Asdf@1234");
            System.out.println("Connected to database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Database connection failed.");
        }
    }

    public void insert_location(double lat, double lon,String rent_amount) {

        String query = "INSERT INTO allocated_locations(latitude,longitude,rent) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, String.valueOf(lat));
            stmt.setString(2, String.valueOf(lon));
            stmt.setString(3, String.valueOf(rent_amount));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Location inserted successfully!");
            } else {
                System.out.println("Location insertion failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to insert user.");
        }
    }

    public void update_location(double lat, double lon,String rent_amount) {
        String query = "UPDATE allocated_locations SET rent = ? WHERE latitude = ? AND longitude = ?";

        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, String.valueOf(rent_amount));
            stmt.setString(2, String.valueOf(lat));
            stmt.setString(3, String.valueOf(lon));

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Location updated successfully!");
            }
            else {
                System.out.println("Location update failed.");
            }
        }
        catch (SQLException e) {
            System.out.println("Failed to update user.");
        }
    }

    public boolean check_location(double lat, double lon) {

        String query = "SELECT * FROM allocated_locations WHERE latitude = ? AND longitude = ?";
//        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND type = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, String.valueOf(lat));
            stmt.setString(2, String.valueOf(lon));

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) return true;
        }
        catch (SQLException e) {
            System.out.println("Checking location failed.");
        }
        return false;
    }

    public void delete_location(double lat, double lon) {
        String query = "DELETE FROM allocated_locations WHERE latitude = ? AND longitude = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1,String.valueOf(lat));
            stmt.setString(2,String.valueOf(lon));

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Location deleted successfully!");
            }
            else {
                System.out.println("Location deletion failed.");
            }
        }
        catch (SQLException e) {
            System.out.println("Location not found.");
        }
    }

    public ArrayList<location_info> getLocations() {
        ArrayList<location_info> locations = new ArrayList<>();

        String query = "SELECT latitude, longitude, rent FROM allocated_locations";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                String rentPrice = rs.getString("rent");

                locations.add(new location_info(latitude, longitude, rentPrice));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving locations: " + e.getMessage());
        }

        return locations;
    }


}
