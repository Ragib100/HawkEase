package me.hawkease;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class admin_bill_sql {
    private Connection con;

    public admin_bill_sql() {
        try{
            con = database_connection.get_connection().sql_connection();
        }
        catch (Exception e) {
            System.out.println("SQL connection could not be established!");
        }
    }

    public boolean insert(String st,String val) {
        String query = "INSERT INTO bills_from_admin(bill,amount) VALUES (?,?)";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1,st);
            stmt.setInt(2, Integer.parseInt(val));
            return stmt.executeUpdate()>0;
        }
        catch(Exception e) {
            System.out.println("SQL connection could not be established!");
        }
        return false;
    }

    public HashMap<String, Integer> get_bills() {
        HashMap<String, Integer> bills = new HashMap<>();
        String query = "SELECT * FROM bills_from_admin";
        try(PreparedStatement stmt = con.prepareStatement(query)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                bills.put(rs.getString("bill"), Integer.valueOf(rs.getString("amount")));
            }
        }
        catch(Exception e) {
            System.out.println("SQL connection could not be established!");
        }
        return bills;
    }

    public boolean delete_bill(String bill) {
        String query = "DELETE FROM bills_from_admin WHERE bill = ?";
        System.out.println("Delete Bill Successfully");
        try(PreparedStatement stmt = con.prepareStatement(query)){
            stmt.setString(1,bill);
//            System.out.println("it is " + stmt.executeUpdate());
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0) {
                System.out.println("Bill deleted successfully");
                return true;
            }
            else {
                System.out.println("Bill could not be deleted");
                return false;
            }
        }
        catch(Exception e) {
            System.out.println("SQL connection could not be established!");
            return false;
        }
    }
}