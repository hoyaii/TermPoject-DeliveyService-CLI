import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DeliveryPerson {
    private Database db;
    private Scanner scanner;
    private int userId;

    public DeliveryPerson(Database db, int userId) {
        this.db = db;
        this.scanner = new Scanner(System.in);
        this.userId = userId;
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

    public void updateDeliveryStatusService(){
        System.out.println("배달 상태를 업데이트할 주문 ID를 입력해 주세요:");
        int orderId = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
        System.out.println("새로운 배달 상태를 입력해 주세요:");
        String newStatus = scanner.nextLine();

        updateDeliveryStatus(orderId, newStatus);
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

    public void getDeliveryHistoryService(int deliveryPersonId){
        ResultSet deliveryHistory = getDeliveryHistory(deliveryPersonId);
        try {
            while (deliveryHistory.next()) {
                int orderId = deliveryHistory.getInt("order_id");
                String restaurantName = deliveryHistory.getString("restaurant_name");
                String customerName = deliveryHistory.getString("customer_name");
                System.out.println("주문 ID: " + orderId + ", 음식점 이름: " + restaurantName + ", 고객 이름: " + customerName);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving delivery history.");
            e.printStackTrace();
        }
    }

    public ResultSet getDeliveryList() { // 엉터리 쿼리
        String sql = "SELECT * FROM Orders WHERE delivery_status = 'Waiting' OR delivery_status = 'In Progress'";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public void getDeliveryListService(){ // 엉터리 쿼리
        ResultSet deliveryList = getDeliveryList();
        try {
            while (deliveryList.next()) {
                int orderId = deliveryList.getInt("order_id");
                String restaurantName = deliveryList.getString("restaurant_name");
                String customerName = deliveryList.getString("customer_name");
                String deliveryStatus = deliveryList.getString("delivery_status");
                System.out.println("주문 ID: " + orderId + ", 음식점 이름: " + restaurantName + ", 고객 이름: " + customerName + ", 배달 상태: " + deliveryStatus);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving delivery list.");
            e.printStackTrace();
        }
    }
}