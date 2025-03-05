package me.hawkease;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
}
