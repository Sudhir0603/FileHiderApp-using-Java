package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection connection = null;

    public static Connection getConnection() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Correct JDBC URL
            String url = "jdbc:mysql://localhost:3306/ytproject?allowPublicKeyRetrieval=true&useSSL=false";
            String username = "root";
            String password = "Sudhir@123";

            // Establish connection
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection ho gaya saab!");

        } catch (ClassNotFoundException e) {
            System.err.println("🚫 JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("🚫 Failed to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✅ Connection closed.");
            } catch (SQLException ex) {
                System.err.println("🚫 Error while closing the connection.");
                ex.printStackTrace();
            }
        }
    }
}
