import java.sql.*;

public class Database {
    private Connection connection;

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

    public ResultSet executeQuery(String sql) {
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public int executeUpdate(String sql) {
        try {
            Statement statement = this.connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error executing SQL update.");
            e.printStackTrace();
            return 0;
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