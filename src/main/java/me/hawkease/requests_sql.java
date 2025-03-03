package me.hawkease;

import java.sql.*;

public class requests_sql {

    private Connection con;

    public requests_sql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://ragib.mysql.database.azure.com:3306/root", "Ragib100", "Asdf@1234");
            System.out.println("Connected to database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Database connection failed.");
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

}
