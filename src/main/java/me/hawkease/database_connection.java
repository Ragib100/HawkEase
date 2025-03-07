package me.hawkease;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database_connection {
    private static volatile database_connection connection;
    private Connection con;

    public static database_connection get_connection() {
        if (connection == null) {
            synchronized (database_connection.class) {
                if (connection == null) {
                    connection = new database_connection();
                }
            }
        }
        return connection;
    }

    public Connection sql_connection() throws SQLException {
        if(con == null || con.isClosed()){
            System.out.println("Connecting to database...");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
//            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ragib", "root", "asdf1234");
                con = DriverManager.getConnection(System.getenv("cloud_sql_host"), System.getenv("cloud_sql_user"), System.getenv("cloud_sql_password"));
                System.out.println("Connected to database.");
            } catch (ClassNotFoundException | SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Database connection failed.");
            }
        }
        return con;
    }
}
