package me.hawkease;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

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

    public boolean update(double lat, double lon,String name,String address,String type) {
        String query = "UPDATE shop_keepers SET shop_name = ?,shop_address = ?,product_type = ? WHERE latitude = ? AND longitude = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1,name);
            stmt.setString(2,address);
            stmt.setString(3,type);
            stmt.setDouble(4,lat);
            stmt.setDouble(5,lon);
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("Editing successful");
            }
            else{
                System.out.println("Editing failed");
            }
            return row > 0;
        }
        catch (Exception e) {
            System.out.println("Error updating locations: ");
        }
        return false;
    }

    public info_for_stall get_stall_info(double lat,double lon){
        String query = "SELECT * FROM shop_keepers WHERE latitude = ? AND longitude = ?";
        info_for_stall temp = null;
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setDouble(1, lat);
            stmt.setDouble(2, lon);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                temp = new info_for_stall(rs.getString("shop_name"),rs.getString("shop_address"),rs.getString("product_type"));
            }
        }
        catch (Exception e) {
            make_alert.getInstance().make_alert("Error","Connection could not be established!");
        }
        return temp;
    }

    public HashMap<String, ArrayList<info_for_buyer_search>> get_info_for_buyer() {
        HashMap<String, ArrayList<info_for_buyer_search>> result = new HashMap<>();
        String query = "SELECT * FROM shop_keepers";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int total = rs.getInt("Total_rating"), num = rs.getInt("number");
                double rating = (num != 0 ? (double) total / num : 0);
                String formattedRating = String.format("%.1f", rating);
                info_for_buyer_search temp = new info_for_buyer_search(
                        rs.getString("shop_name"),
                        rs.getString("email"),
                        rs.getString("shop_address"),
                        formattedRating,
                        rs.getInt("number")
                );

                String productType = rs.getString("product_type");
                result.putIfAbsent(productType, new ArrayList<>()); // 🔥 Ensure ArrayList is initialized
                result.get(productType).add(temp);
            }
        } catch (Exception e) {
            make_alert.getInstance().make_alert("Error", "Connection could not be established!");
        }
        return result;
    }

    public boolean update_rating(int rat) {
        String query = "UPDATE shop_keepers SET rating = rating+? WHERE email = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setInt(1, rat);
            stmt.setString(2, current_user.get_user().get_email());
            int row = stmt.executeUpdate();
            return row > 0;
        }
        catch (Exception e) {

        }
        return false;
    }
}
