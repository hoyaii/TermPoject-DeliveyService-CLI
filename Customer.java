import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Customer {
    private Database db;
    private Scanner scanner;
    private int userId;

    public Customer(Database db, int userId) {
        this.db = db;
        this.scanner = new Scanner(System.in);
        this.userId = userId;
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

    public void searchRestaurantsService(int userId){
        System.out.println("검색하실 음식점의 이름을 입력해 주세요:");
        String name = scanner.nextLine();
        System.out.println("검색하실 음식점의 위치를 입력해 주세요:");
        String location = scanner.nextLine();
        System.out.println("검색하실 음식점의 음식 종류를 입력해 주세요:");
        String type = scanner.nextLine();

        ResultSet resultSet = searchRestaurants(name, location, type);
        HashSet<Integer> validRestaurantIds = new HashSet<>();
        try {
            while (resultSet.next()) {
                int restaurantId = resultSet.getInt("restaurant_id");
                validRestaurantIds.add(restaurantId);
                System.out.println("음식점 ID: " + resultSet.getInt("restaurant_id"));
                System.out.println("이름: " + resultSet.getString("name"));
                System.out.println("위치: " + resultSet.getString("location"));
                System.out.println("음식 종류: " + resultSet.getString("type"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("ResultSet에서 읽는 중 에러가 발생했습니다.");
            e.printStackTrace();
        }

        System.out.println("메뉴를 확인하고 싶은 음식점의 ID를 입력해주세요.");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

        if (validRestaurantIds.contains(restaurantId)) {
            orderService(restaurantId, userId);
        } else {
            System.out.println("유효하지 않은 음식점 ID입니다.");
        }
    }

    public boolean requestDelivery(int restaurantId, String address){
        String serviceArea = getServiceArea(restaurantId);

        List<Integer> availableDeliveryPersons =  getAvailableDeliveryPeople(serviceArea);

        if(availableDeliveryPersons.isEmpty()){
            System.out.println("배달 가능한 배달원이 존재하지 않습니다.");
            return false;
        }

        Random rand = new Random(); // 가능한 기사중 아무나 한명을 선택한다
        int randomIndex = rand.nextInt(availableDeliveryPersons.size());
        Integer selectedDeliveryPersonId = availableDeliveryPersons.get(randomIndex);

        createDelivery(restaurantId, address, selectedDeliveryPersonId);

        return true;
    }

    public void createDelivery(int restaurantId, String address, int deliveryPersonId) {
        String sql = "INSERT INTO Delivery (restaurant_id, delivery_address, delivery_person_id, status) VALUES (?, ?, ?, ?)";
        String status = "notAccepted";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setString(2, address);
            preparedStatement.setInt(3, deliveryPersonId);
            preparedStatement.setString(4, status);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL 쿼리 실행 중 오류가 발생했습니다.");
            e.printStackTrace();
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

    public boolean createOrder(int restaurantId, int menuId) {
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

    public void orderService(int restaurantId, int userId){
        ResultSet menuResultSet = getMenu(restaurantId);
        try {
            while (menuResultSet.next()) {
                System.out.println("메뉴 ID: " + menuResultSet.getInt("menu_id"));
                System.out.println("메뉴 이름: " + menuResultSet.getString("name"));
                System.out.println("가격: " + menuResultSet.getDouble("price"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("ResultSet에서 읽는 중 에러가 발생했습니다.");
            e.printStackTrace();
        }

        System.out.println("주문하실 메뉴의 ID를 입력해 주세요:");
        int menuId = scanner.nextInt();
        scanner.nextLine();

        String address = getUserAddress(userId);
        boolean requestSuccess = requestDelivery(restaurantId, address); // 배달 요청 -> 배달 기사 없으면 실패
        if (!requestSuccess) {
            System.out.println("주문에 실패하였습니다.");
        }

        boolean orderSuccess = createOrder(restaurantId, menuId); // 주문 생성
        if (!orderSuccess) {
            System.out.println("주문에 실패하였습니다.");
        }

        System.out.println("주문이 성공적으로 완료되었습니다.");

        // 음식점 주인이 배달원을 요청하고, 배달원이 승낙하여 매칭되어 배달하고 완료
        // 레스토랑의 service_area를 바탕으로 해당 지역의 프리한 배달원들의 리스트들을 반환한다.
        // 배달원은 요청 리스트들 중에서 하나를 승낙한다.
    }

    public String getDeliveryStatus(int orderId) {
        String sql = "SELECT order_status FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("order_status");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public void getDeliveryStatusService(int userId){
        List<Integer> userOrders = getUserOrders(userId);

        if (userOrders.isEmpty()) {
            System.out.println("주문이 없습니다.");
            return;
        }

        for (Integer orderId : userOrders) {
            System.out.println("메뉴명: " + getMenuNameFromOrder(orderId) + ", 주문 ID: " + orderId);
        }

        System.out.println("확인하고 싶은 주문의 ID를 입력해주세요:");
        int orderId = scanner.nextInt();
        scanner.nextLine();

        if (!userOrders.contains(orderId)) {
            System.out.println("선택하신 주문 ID는 유효하지 않습니다.");
            return;
        }

        String deliveryStatus = getDeliveryStatus(orderId);
        String orderTime = getOrderTime(orderId);
        if (deliveryStatus != null) {
            System.out.println("배달 상태: " + deliveryStatus);
            System.out.println("주문 시간: " + orderTime);

        } else {
            System.out.println("주문 ID가 잘못되었거나, 배달 상태를 확인할 수 없습니다.");
        }
    }

    public void writeReview(String restaurantName, int rating, String reviewContent) {
        String sql = "INSERT INTO Reviews (restaurant_name, rating, content) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, restaurantName);
            preparedStatement.setInt(2, rating);
            preparedStatement.setString(3, reviewContent);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("리뷰가 성공적으로 작성되었습니다.");
            } else {
                System.out.println("리뷰 작성에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void writeReviewService(int userId){
        System.out.println("리뷰를 작성할 음식점의 이름을 입력해 주세요:");
        String restaurantName = scanner.nextLine();
        System.out.println("리뷰의 별점을 입력해 주세요:");
        int rating = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
        System.out.println("리뷰 내용을 입력해 주세요:");
        String reviewContent = scanner.nextLine();

        writeReview(restaurantName, rating, reviewContent);
    }

    public List<Integer> getUserOrders(int userId) {
        String sql = "SELECT order_id FROM Orders WHERE user_id = ?";
        List<Integer> orderIds = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                orderIds.add(resultSet.getInt("order_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderIds;
    }

    public String getMenuNameFromOrder(int orderId) {
        String sql = "SELECT m.name FROM Orders o JOIN Menu m ON o.menu_id = m.menu_id WHERE o.order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public String getOrderTime(int orderId) {
        String sql = "SELECT order_time FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("order_time");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.format(timestamp);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> getAvailableDeliveryPeople(String serviceArea) {
        List<Integer> availableDeliveryPersons = new ArrayList<>();
        String sql = "SELECT delivery_person_id FROM DeliveryPerson WHERE service_area = ? AND status = 'Free'";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, serviceArea);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                availableDeliveryPersons.add(resultSet.getInt("delivery_person_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
        return availableDeliveryPersons;
    }

    public String getServiceArea(int restaurantId){
        String sql = "SELECT service_area FROM Restaurant WHERE restaurant_id = ?";

        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("restaurant_id");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public String getUserAddress(int userId) {
        String address = null;
        String sql = "SELECT address FROM User WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                address = resultSet.getString("address");
            }
        } catch (SQLException e) {
            System.out.println("SQL 쿼리 실행 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return address;
    }
}