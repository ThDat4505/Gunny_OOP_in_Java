package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class JDBCConnection {
    private static Connection conn = null;

    public static Connection getConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) return conn;


        String url  = "jdbc:mysql://127.0.0.1:3306/gunny?user=root";
        String user = "root";
        String pass = "root1234";

        conn = DriverManager.getConnection(url, user, pass);
        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}