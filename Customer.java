import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    private Database db;
    private Scanner scanner;

    public Customer(Database db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }

    public ResultSet searchRestaurants(String name, String location, String type) {
        String sql = "SELECT * FROM Restaurant WHERE name LIKE ? AND location LIKE ? AND type LIKE ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setString(2, "%" + location + "%");
            preparedStatement.setString(3, "%" + type + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }
}