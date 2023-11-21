import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class RestaurantOwner {
    private Database db;
    private int ownerId;

    public RestaurantOwner(Database db, int ownerId) {  // ownerId를 받는 생성자 추가
        this.db = db;
        this.ownerId = ownerId;  // ownerId 초기화
    }

    public Integer getRestaurantIdByName(String name) {
        String sql = "SELECT restaurant_id FROM Restaurant WHERE name = ? AND owner_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, this.ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("restaurant_id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerRestaurant(String name, String address, String cuisineType) {
        String sql = "INSERT INTO Restaurant (name, address, cuisine_type, owner_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, cuisineType);
            preparedStatement.setInt(4, this.ownerId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRestaurantInfo(int restaurantId, String name, String address, String cuisineType) {
        String sql = "UPDATE Restaurant SET name = ?, address = ?, cuisine_type = ? WHERE owner_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, cuisineType);
            preparedStatement.setInt(4, restaurantId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return false;
        }
    }

    public void registerMenu(int restaurantId, String name, double price) { // 메뉴 및 가격 등록
        String sql = "INSERT INTO Menu (name, price, restaurant_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, restaurantId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void updateMenu(int menuId, String name, double price) { // 메뉴 및 가격 업데이트
        String sql = "UPDATE Menu SET name = ?, price = ? WHERE menu_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, menuId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void processOrder(int orderId, String status) {
        String sql = "UPDATE `Order` SET order_status = ? WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public ResultSet getOrderHistory(int restaurantId) {
        String sql = "SELECT * FROM `Order` WHERE menu_id IN (SELECT menu_id FROM Menu WHERE restaurant_id = ?)";
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
}
