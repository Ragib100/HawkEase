package me.hawkease;

import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class seller_bill_sql {
    private Connection con;

    public seller_bill_sql() {
        try{
            con = database_connection.get_connection().sql_connection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean insert(String email,String bill,Double latitude,Double longitude) {
        String query = "INSERT INTO bills_from_seller(email,bill,latitude,longitude) VALUES (?, ?, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, email);
            stmt.setString(2, bill);
            stmt.setDouble(3, latitude);
            stmt.setDouble(4, longitude);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
        catch(Exception e){
            return false;
        }
    }

    public boolean delete(String bill){
        String query = "DELETE FROM bills_from_seller WHERE bill = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, bill);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
        catch(Exception e){
            return false;
        }
    }

    public Pair<String,location_info> get_location(String bill) {
        String query = "SELECT email,latitude, longitude FROM bills_from_seller WHERE bill = ?";
        location_info location;
        Pair<String,location_info> res = new Pair<>(null,null);

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, bill);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String email = rs.getString("email");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                location = new location_info(latitude, longitude);
                res = new Pair<>(email,location);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public boolean check(String key){
        String query = "SELECT * FROM bills_from_seller WHERE bill = ?";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println("key found");
                return true;
            }
        }
        catch (Exception e){
            System.out.println("key not found");
        }
        return false;
    }
}