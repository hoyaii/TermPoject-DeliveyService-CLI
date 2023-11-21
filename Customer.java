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

    public ResultSet getMenu(int restaurantId) {
        String sql = "SELECT * FROM Menu WHERE restaurant_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean orderMenu(int restaurantId, int menuId) {
        String sql = "INSERT INTO Orders (restaurant_id, menu_id) VALUES (?, ?)"; // ******** 수정 필요, 배달원과 동기화
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setInt(2, menuId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return false;
        }
    }

    public String getDeliveryStatus(int orderId) {
        String sql = "SELECT delivery_status FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("delivery_status");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }
}