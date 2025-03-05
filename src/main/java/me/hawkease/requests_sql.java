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

    public void request_location(double lat, double lon){
        String email = current_user.get_user().get_email();
        if(email == null) {
            System.out.println("Email is null");
            return;
        }
        else System.out.println("Email: " + email);
        String query = "INSERT INTO requests(email,latitude,longitude) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setDouble(2, lat);
            stmt.setDouble(3, lon);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("request inserted successfully!");
            }
            else {
                System.out.println("request insertion failed.");
            }
        }
        catch(SQLException e){
            System.out.println("Error in request_location");
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

    public void delete_request(String email){
        String query = "DELETE FROM requests WHERE email = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) System.out.println("request deleted successfully!");
            else System.out.println("request deletion failed.");
        }
        catch(SQLException e){
            System.out.println("Error in delete_request");
        }
    }

}
