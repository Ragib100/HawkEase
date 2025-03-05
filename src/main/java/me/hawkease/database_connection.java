package me.hawkease;

import java.sql.Connection;

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

    public void set_connection(Connection con) {
        if (con != null) {
            this.con = con;
        }
    }

    public Connection sql_connection() throws Exception {
        if(con == null) throw new Exception("Database not connected");
        return con;
    }
}
