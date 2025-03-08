package me.hawkease;

import java.sql.*;
import java.util.ArrayList;

public class requests_sql {

    private Connection con;

    public requests_sql() {
        try{
            con = database_connection.get_connection().sql_connection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean request_location(double lat, double lon){
        String email = current_user.get_user().get_email();
        if(check(email, lat, lon)) return false;
        if(email == null) {
            System.out.println("Email is null");
            return false;
        }
        shop_keepers_sql shop_keepers_sql = new shop_keepers_sql();
        if(!shop_keepers_sql.is_under_3(email)){
            System.out.println("Email is not under 3");
            return false;
        }
        else System.out.println("Email: " + email);
        String query = "INSERT INTO requests(email,latitude,longitude) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
        catch(SQLException e){
            return false;
        }
    }

    public ArrayList<location_requests> get_requests() {
        ArrayList<location_requests> requests = new ArrayList<>();
        String query = "SELECT email, latitude, longitude FROM requests";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                String email = rs.getString("email");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                requests.add(new location_requests(email, lat, lon));
            }
        }
        catch(SQLException e){
            System.out.println("Error in getting request_location");
        }
        return requests;
    }

    public boolean delete_request(String email,double lat,double lon){
        String query = "DELETE FROM requests WHERE email = ? AND latitude = ? AND longitude = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) return true;
            else return false;
        }
        catch(SQLException e){
            return false;
        }
    }

    public boolean check(String email, double lat, double lon){
        String query = "SELECT email, latitude, longitude FROM requests WHERE email = ? AND latitude = ? AND longitude = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e){
            System.out.println("Error in check");
            return true;
        }
    }
}
