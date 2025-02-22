package me.hawkease;

import java.sql.*;

public class users_sql {
    private Connection con;

    public users_sql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://ragib.mysql.database.azure.com:3306/root", "Ragib100", "Asdf@1234");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ragib", "root", "asdf1234");
            System.out.println("Connected to database.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Database connection failed.");
        }
    }

    public void insert_user(String username, String password, String email, String type) {
        String query = "INSERT INTO users(email, name, password, type) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, type);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User inserted successfully!");
            } else {
                System.out.println("User insertion failed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to insert user.");
        }
    }

    public boolean check_user(String email, String password,String type) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ? AND type = ?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1, email);
            st.setString(2, password);
            st.setString(3, type);
            ResultSet rs = st.executeQuery();
            if(rs.next()) return true;
            else return false;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void change_password(String email, String password, String type) {
        System.out.println(email + " " + password + " " + type);
        String query = "UPDATE users SET password = ? WHERE email = ? AND type = ?";
        try(PreparedStatement st = con.prepareStatement(query)){
            st.setString(1, password);
            st.setString(2, email);
            st.setString(3, type);
            int rowsUpdated = st.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Password changed successfully!");
            }
            else {
                System.out.println("Password change failed.");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
