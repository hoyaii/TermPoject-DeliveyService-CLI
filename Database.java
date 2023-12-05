import java.sql.*;

public class Database {
    Connection connection;

    public Database(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully connected to MySQL database!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to MySQL database.");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing MySQL connection.");
            e.printStackTrace();
        }
    }
}
