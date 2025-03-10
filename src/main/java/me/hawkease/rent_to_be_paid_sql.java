package me.hawkease;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class rent_to_be_paid_sql {
    private Connection con;

    public rent_to_be_paid_sql() {
        try{
            con = database_connection.get_connection().sql_connection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean update_bill(String email, Double lat, Double lon, int paymentAmount) {
        String query = "UPDATE rent_to_be_paid SET amount = amount - ? WHERE email = ? AND latitude = ? AND longitude = ?";
        System.out.println("here");
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, paymentAmount);
            stmt.setString(2, email);
            stmt.setDouble(3, lat);
            stmt.setDouble(4, lon);
            int result = stmt.executeUpdate();
            if(result > 0) {
                System.out.println("rent to paid successfully");
            }
            else{
                System.out.println("rent to paid failed");
            }
            return result>0;
        }
        catch (SQLException e) {
            System.err.println("Error updating bill: ");
            return false;
        }
    }

    public boolean insert(String email,Double lat,Double lon){
        String query = "INSERT INTO rent_to_be_paid(latitude,longitude,email) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1, String.valueOf(lat));
            stmt.setString(2, String.valueOf(lon));
            stmt.setString(3, email);
            int result = stmt.executeUpdate();
            return result>0;
        }
        catch (Exception e){

        }
        return false;
    }
}
