import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryPerson {
    private Database db;

    public DeliveryPerson(Database db) {
        this.db = db;
    }

    public void updateDeliveryStatus(int orderId, String status) {
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

    public ResultSet getDeliveryHistory(int deliveryPersonId) {
        String sql = "SELECT * FROM `Order` WHERE delivery_person_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, deliveryPersonId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }
}