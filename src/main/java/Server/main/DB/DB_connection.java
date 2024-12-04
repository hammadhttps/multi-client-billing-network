package Server.main.DB;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_connection {

    private static final String URL = "jdbc:mysql://localhost:3306/billing_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    public static void main(String[] args) {
        try {

            Connection connection = DB_connection.getConnection();

        } catch (SQLException e) {
            // Handle the exception
            System.out.println("Failed to create the database connection.");
        }
    }
}
