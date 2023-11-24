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

    public ResultSet getDeliveryList(int userId, String status){
        String sql = "SELECT * FROM Delivery WHERE delivery_person_id = ? AND status = ?";
        try{
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public void getDeliveryRequestService(int userId){ //  배달원이 요청 리스트를 보고 승낙하여 매칭
        System.out.println("요청이 들어온 배달 목록들입니다. 수락하고 싶은 배달을 선택해 주세요");
        ResultSet resultSet = getDeliveryList(userId, "notAccepted");

        try {
            if(resultSet.wasNull()){
                System.out.println("요청이 들어온 배달 요청이 없습니다.");
                return;
            }

            while (resultSet.next()) {
                int deliveryId = resultSet.getInt("delivery_id ");
                int restaurantId = resultSet.getInt("restaurant_id");
                String deliveryAddress = resultSet.getString("delivery_address");
                String restaurantAddress = getRestaurantAddress(restaurantId);
                System.out.println("id: " + deliveryId + " 가게 주소: " + restaurantAddress + " 배달할 주소: " + deliveryAddress);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving delivery history.");
            e.printStackTrace();
        }

        System.out.println("수락하고 싶은 요청의 id를 입력하세요.");
        int deliveryId = scanner.nextInt();
        scanner.nextLine();
        updateDeliveryStatus("accepted", deliveryId);
    }

    public void updateDeliveryStatus(String status, int deliveryId) {
        String sql = "UPDATE Delivery SET status = ? WHERE delivery_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void updateOrderStatus(String status, int orderId) {
        String sql = "UPDATE `Order` SET status = ? WHERE order_id = ?";
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

    public void finishDeliveryService(int userId){
        System.out.println("진행중인 배달 내역입니다.");
        ResultSet resultSet = getDeliveryList(userId, "accepted");

        try {
            if(resultSet.wasNull()){
                System.out.println("배달 목록이 없습니다.");
                return;
            }

            while (resultSet.next()) {
                int deliveryId = resultSet.getInt("delivery_id ");
                int restaurantId = resultSet.getInt("restaurant_id");
                String deliveryAddress = resultSet.getString("delivery_address");
                String restaurantAddress = getRestaurantAddress(restaurantId);
                System.out.println("id: " + deliveryId + " 가게 주소: " + restaurantAddress + " 배달할 주소: " + deliveryAddress);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("완료한 배달의 ID를 입력해 주세요:");
        int deliveryId = scanner.nextInt();
        scanner.nextLine();

        Integer orderId = getOrderIdByDeliveryId(deliveryId); // deliveryId를 가지고 orderId를 구한다

        updateDeliveryStatus("finished", deliveryId);
        updateOrderStatus("finished", orderId);
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

    public String getRestaurantAddress(int restaurantId) {
        String sql = "SELECT address FROM Restaurant  WHERE restaurant_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("address");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public Integer getOrderIdByDeliveryId(int deliveryId) {
        String sql = "SELECT order_id FROM Orders WHERE delivery_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, deliveryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("order_id");
            }
            else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL 쿼리 실행 중 오류가 발생했습니다.");
            e.printStackTrace();
            return null;
        }
    }
}