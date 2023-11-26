import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public ResultSet getDeliveryList(String status){
        String sql = "SELECT * FROM Delivery WHERE delivery_person_id = ? AND status = ?";
        try{
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, status);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public void getDeliveryRequestService(){ //  배달원이 요청 리스트를 보고 승낙하여 매칭
        System.out.println("요청이 들어온 배달 목록들입니다. 수락하고 싶은 배달을 선택해 주세요");
        ResultSet resultSet = getDeliveryList("notAccepted");

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

        Integer orderId = getOrderIdByDeliveryId(deliveryId);

        updateDeliveryStatus("accepted", deliveryId);
        updateOrderStatus("deliveryMatched", orderId);
        updateUserStatus("notFree");
        System.out.println("요청이 수락되었습니다.");
    }

    public void updateDeliveryStatus(String status, int deliveryId) {
        String sql = "UPDATE Delivery SET status = ? WHERE delivery_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, deliveryId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateUserStatus(String status) {
        String sql = "UPDATE User SET status = ? WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateOrderStatus(String status, int orderId) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
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

    public void finishDeliveryService(){
        System.out.println("진행중인 배달 내역입니다.");

        ResultSet resultSet = getDeliveryList("accepted");
        List<Integer> deliveryIdList = printDeliveryList(resultSet);

        if(deliveryIdList.isEmpty()){
            return;
        }

        Integer deliveryId;
        do {
            System.out.println("완료한 배달의 ID를 입력해 주세요:");
            deliveryId = scanner.nextInt();
            scanner.nextLine();

            if(!deliveryIdList.contains(deliveryId)){
                System.out.println("선택하신 배달 ID는 유효하지 않습니다.");
            }
        } while(!deliveryIdList.contains(deliveryId));

        Integer orderId = getOrderIdByDeliveryId(deliveryId); // deliveryId를 가지고 orderId를 구한다

        updateDeliveryStatus("finished", deliveryId);
        updateOrderStatus("finished", orderId);
        updateUserStatus("free");

        System.out.println("배달 완료 처리가 되었습니다.");
    }

    public List<Integer> printDeliveryList(ResultSet resultSet){
        List<Integer> deliveryIdList = new ArrayList<>();
        try {
            if(resultSet.wasNull()){
                System.out.println("배달 목록이 없습니다.");
                return null;
            }

            while (resultSet.next()) {
                int deliveryId = resultSet.getInt("delivery_id ");
                int restaurantId = resultSet.getInt("restaurant_id");
                String deliveryAddress = resultSet.getString("delivery_address");
                String restaurantAddress = getRestaurantAddress(restaurantId);
                System.out.println("id: " + deliveryId + " 가게 주소: " + restaurantAddress + " 배달할 주소: " + deliveryAddress);

                deliveryIdList.add(deliveryId);
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }

        return deliveryIdList;
    }

    public ResultSet getDeliveryHistory() {
        String sql = "SELECT * FROM Orders WHERE delivery_person_id = ? AND status = 'finished'";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public void getDeliveryHistoryService(){
        ResultSet resultSet = getDeliveryHistory();
        try {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int menuId = resultSet.getInt("menu_id");
                int restaurantId = resultSet.getInt("restaurant_id");
                Timestamp orderTime = resultSet.getTimestamp("order_time");
                LocalDateTime orderDateTime = orderTime.toLocalDateTime();
                String formattedOrderTime = orderDateTime.toString();

                String menuName = getMenuName(menuId);
                String restaurantName = getRestaurantName(restaurantId);
                System.out.println("주문 ID: " + orderId + ", 가게 이름: " + restaurantName + "메뉴 이름: " + menuName + "주문 시간: " + formattedOrderTime);
            }
        } catch (SQLException e) {
            handleSQLException(e);
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
            handleSQLException(e);
            return null;
        }
    }

    public String getRestaurantName(int restaurantId) {
        String sql = "SELECT name FROM Restaurant  WHERE restaurant_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
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
            handleSQLException(e);
            return null;
        }
    }

    public String getMenuName(int menuId) {
        String sql = "SELECT name  FROM Menu WHERE menu_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, menuId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    private void handleSQLException(SQLException e) {
        System.out.println("SQL 쿼리 실행에서 에러가 발생하였습니다.");
        e.printStackTrace();
    }
}