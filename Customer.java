import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    public void searchRestaurantsService(){ // ***************** 3가지 쿼리 기능 테스트 필요
        String serviceArea;
        do {
            System.out.println("검색하실 음식점의 지역을 입력해 주세요:");
            serviceArea = scanner.nextLine();

            if (!serviceArea.equals("서울") && !serviceArea.equals("부산") && !serviceArea.equals("대구") && !serviceArea.equals("대전") && !serviceArea.equals("광주") && !serviceArea.equals("울산")) {
                System.out.println("배달 서비스는 '서울', '부산', '대구', '대전', '광주', '울산'에서만 제공합니다. 다시 작성해주세요.");
                serviceArea = null;
            }
        } while (serviceArea == null);

        System.out.println("검색하실 음식의 종류를 입력해 주세요: (옵션)");
        String type = scanner.nextLine();

        System.out.println("검색하실 음식점의 이름을 입력해 주세요: (옵션)");
        String name = scanner.nextLine();

        ResultSet resultSet = getRestaurants(name, serviceArea, type);
        List<Integer> restaurantIdList= printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){
            System.out.println("음식점 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("메뉴를 확인하고 싶은 음식점의 ID를 입력해주세요.");
            restaurantId = scanner.nextInt();
            scanner.nextLine();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        orderService(restaurantId);
    }

    public void orderService(int restaurantId){
        ResultSet resultSet = getMenu(restaurantId);
        List<Integer> menuIdList = printMenuList(resultSet); // 메뉴들 출력

        if(menuIdList.isEmpty()){ // 메뉴가 없는 경우
            System.out.println("메뉴가 준비되어 있지 않습니다. 나중에 다시 시도해주세요!:");
            return;
        }

        Integer menuId;
        do {
            System.out.println("주문하실 메뉴의 ID를 입력해 주세요:");
            menuId = scanner.nextInt();
            scanner.nextLine();

            if(!menuIdList.contains(menuId)){
                System.out.println("선택하신 메뉴 ID는 유효하지 않습니다.");
            }
        } while(!menuIdList.contains(menuId));

        String address = getUserAddress(userId);

        int deliveryId = requestDeliveryService(restaurantId, address); // 배달 요청 -> 배달 기사 없으면 실패
        if (deliveryId == 0) {
            System.out.println("주문에 실패하였습니다.");
            return;
        }

        boolean orderSuccess = createOrder(deliveryId, restaurantId, menuId); // 주문 생성
        if (!orderSuccess) {
            System.out.println("주문에 실패하였습니다.");
            return;
        }

        System.out.println("주문이 성공적으로 요청되었습니다.");
    }

    public int requestDeliveryService(int restaurantId, String address){
        String serviceArea = getServiceArea(restaurantId);

        List<Integer> availableDeliveryPersons = getAvailableDeliveryPeople(serviceArea);

        if(availableDeliveryPersons.isEmpty()){
            System.out.println("배달 가능한 배달원이 존재하지 않습니다.");
            return 0;
        }

        Random rand = new Random(); // 가능한 기사중 아무나 한명을 선택한다
        int randomIndex = rand.nextInt(availableDeliveryPersons.size());
        Integer selectedDeliveryPersonId = availableDeliveryPersons.get(randomIndex);

        int deliveryId = createDelivery(restaurantId, address, selectedDeliveryPersonId);

        return deliveryId;
    }

    public void getDeliveryStatusService(){
        ResultSet resultSet = getUserOrders("");
        List<Integer> orderIdList = printOrderHistory(resultSet);

        if(orderIdList.isEmpty()){
            System.out.println("주문 내역이 존재하지 않습니다. 주문 후 이용해주세요!");
            return;
        }

        Integer orderId;
        do {
            System.out.println("확인하고 싶은 주문의 ID를 입력해주세요:");
            orderId = scanner.nextInt();
            scanner.nextLine();

            if(!orderIdList.contains(orderId)){
                System.out.println("선택하신 주문 ID는 유효하지 않습니다.");
            }
        } while(!orderIdList.contains(orderId));

        String deliveryStatus = getDeliveryStatus(orderId);

        System.out.println("해당 주문의 배달 상태는 " + deliveryStatus + "입니다!");
    }

    public void writeReviewService(){
        ResultSet resultSet = getUserOrders("finished");
        List<Integer> orderIdList = printOrderHistory(resultSet);

        if(orderIdList.isEmpty()){
            System.out.println("주문 내역이 존재하지 않습니다. 주문 후 이용해주세요!");
            return;
        }

        Integer orderId;
        do {
            System.out.println("리뷰를 작성할 주문의 ID를 입력해 주세요:");
            orderId = scanner.nextInt();
            scanner.nextLine();

            if(!orderIdList.contains(orderId)){
                System.out.println("선택하신 주문 ID는 유효하지 않습니다.");
            }
        } while(!orderIdList.contains(orderId));

        int rating;
        do {
            System.out.println("리뷰의 별점을 입력해 주세요 (1~5):");
            rating = scanner.nextInt();
            if (rating < 1 || rating > 5) {
                System.out.println("잘못 입력하셨습니다. 별점은 1~5 사이의 숫자로 입력해 주세요.");
            }
        } while (rating < 1 || rating > 5);

        scanner.nextLine();

        String reviewContent;
        do {
            System.out.println("리뷰 내용을 입력해 주세요:");
            reviewContent = scanner.nextLine();
            if (reviewContent.trim().isEmpty()) {
                System.out.println("리뷰 내용이 입력되지 않았습니다. 리뷰 내용을 입력해주세요.");
            }
        } while (reviewContent.trim().isEmpty());

        int menuId = getMenuIdByOrderId(orderId);

        createReview(orderId, rating, reviewContent, menuId);
    }

    public ResultSet getRestaurants(String name, String serviceArea, String cuisineType) {
        String sql = "SELECT * FROM Restaurant WHERE name LIKE ? AND service_area  LIKE ? AND cuisine_type LIKE ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");
            preparedStatement.setString(2, "%" + serviceArea + "%");
            preparedStatement.setString(3, "%" + cuisineType + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public List<Integer> printRestaurantList(ResultSet resultSet){
        List<Integer> restaurantIdList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int restaurantId = resultSet.getInt("restaurant_id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("service_area");
                String type = resultSet.getString("cuisine_type");

                System.out.println("음식점 ID: " + restaurantId + "| 이름: " + name + "| 위치: " + location + "| 음식 종류: " + type);

                restaurantIdList.add(restaurantId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return restaurantIdList;
    }

    public int createDelivery(int restaurantId, String address, int deliveryPersonId) {
        String sql = "INSERT INTO Delivery (restaurant_id, delivery_address, delivery_person_id, status) VALUES (?, ?, ?, ?)";
        String status = "notAccepted";
        int deliveryId = 0;
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setString(2, address);
            preparedStatement.setInt(3, deliveryPersonId);
            preparedStatement.setString(4, status);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                deliveryId = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return deliveryId;
    }

    public ResultSet getMenu(int restaurantId) {
        String sql = "SELECT * FROM Menu WHERE restaurant_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public boolean createOrder(int deliveryId, int restaurantId, int menuId) {
        String sql = "INSERT INTO Orders (delivery_id, restaurant_id, menu_id, customer_id, status, order_time) VALUES (?, ?, ?, ?, ?, ?)";

        // order_time 구하기
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, deliveryId);
            preparedStatement.setInt(2, restaurantId);
            preparedStatement.setInt(3, menuId);
            preparedStatement.setInt(4, userId);
            preparedStatement.setString(5, "notMatched");
            preparedStatement.setTimestamp(6, timestamp);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public List<Integer> printMenuList(ResultSet resultSet){
        List<Integer> menuIdList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int menuId = resultSet.getInt("menu_id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                System.out.println("메뉴 ID: " + menuId + "| 메뉴 이름: " + name + "| 가격: " + price);

                menuIdList.add(menuId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return menuIdList;
    }

    public String getDeliveryStatus(int orderId) {
        String sql = "SELECT status FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("status");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public List<Integer> printOrderHistory(ResultSet resultSet){
        List<Integer> orderIdList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String menuName = getMenuNamByOrderId(orderId);
                Timestamp orderTime = resultSet.getTimestamp("order_time");

                LocalDateTime orderDateTime = orderTime.toLocalDateTime();
                String formattedOrderTime = orderDateTime.toString();

                System.out.println("주문 ID: " + orderId + "| 메뉴명: " + menuName + "| 주문 시간: " + formattedOrderTime);

                orderIdList.add(orderId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return orderIdList;
    }

    public void createReview(int orderId, int rating, String reviewContent, int menuId) {
        String sql = "INSERT INTO Review (order_id, rating, content, menu_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, rating);
            preparedStatement.setString(3, reviewContent);
            preparedStatement.setInt(4, menuId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("리뷰가 성공적으로 작성되었습니다.");
            } else {
                System.out.println("리뷰 작성에 실패하였습니다.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public ResultSet getUserOrders(String status) {
        String sql = "SELECT * FROM Orders WHERE customer_id = ? AND status LIKE ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, "%" + status + "%");
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public String getMenuNamByOrderId(int orderId) {
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
            handleSQLException(e);
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
            handleSQLException(e);
            return null;
        }
    }

    public List<Integer> getAvailableDeliveryPeople(String serviceArea) {
        List<Integer> availableDeliveryPersons = new ArrayList<>();
        String sql = "SELECT user_id FROM User WHERE service_area = ? AND status = 'free'";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, serviceArea);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                availableDeliveryPersons.add(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            handleSQLException(e);
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
                return resultSet.getString("service_area");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public String getUserAddress(int userId) {
        String sql = "SELECT address FROM User WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("address");
            } else{
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public int getMenuIdByOrderId(int orderId) {
        String sql = "SELECT menu_id FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("menu_id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    private void handleSQLException(SQLException e) {
        System.out.println("SQL 쿼리 실행에서 에러가 발생하였습니다.");
        e.printStackTrace();
    }
}