import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RestaurantOwner {
    private Database db;
    private Scanner scanner;
    private int userId; // 사장님 id

    public RestaurantOwner(Database db, int userId) {
        this.db = db;
        this.scanner = new Scanner(System.in);
        this.userId = userId;
    }

    public void registerRestaurantService(){
        System.out.println("등록할 음식점의 이름을 입력해 주세요:");
        String name = scanner.nextLine();
        while(name == null || name.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            name = scanner.nextLine();
        }

        System.out.println("등록할 음식점의 위치를 입력해 주세요:");
        String address = scanner.nextLine();
        while(address == null || address.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            address = scanner.nextLine();
        }

        System.out.println("등록할 음식점의 음식 종류를 입력해 주세요:");
        String cuisineType = scanner.nextLine();
        while(cuisineType == null || cuisineType.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            cuisineType = scanner.nextLine();
        }

        String serviceArea;
        do {
            System.out.println("배달 서비스를 제공할 지역을 입력해 주세요:");
            serviceArea = scanner.nextLine();

            if (!serviceArea.equals("서울") && !serviceArea.equals("부산") && !serviceArea.equals("대구") && !serviceArea.equals("대전") && !serviceArea.equals("광주") && !serviceArea.equals("울산")) {
                System.out.println("배달 서비스는 '서울', '부산', '대구', '대전', '광주', '울산'에서만 제공합니다. 다시 작성해주세요.");
                serviceArea = null;
            }
        } while (serviceArea == null);

        boolean registerSuccess = registerRestaurant(name, address, cuisineType, serviceArea);
        if (registerSuccess) {
            System.out.println("음식점 정보가 성공적으로 등록되었습니다.");
        } else {
            System.out.println("음식점 정보 등록에 실패하였습니다.");
        }
    }

    public void updateRestaurantInfoService(){
        ResultSet resultSet = getRestaurantList();
        List<Integer> restaurantIdList = printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){ // 식당이 없는 경우
            System.out.println("관리하고 있는 식당 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("정보를 업데이트할 음식점의 id를 입력해 주세요:");
            restaurantId = scanner.nextInt();
            scanner.nextLine();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        System.out.println("음식점의 새로운 이름을 입력해 주세요:");
        String newName = scanner.nextLine();
        while(newName == null || newName.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newName = scanner.nextLine();
        }

        System.out.println("음식점의 새로운 위치를 입력해 주세요:");
        String newAddress = scanner.nextLine();
        while(newAddress == null || newAddress.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newAddress = scanner.nextLine();
        }

        System.out.println("음식점의 새로운 카테고리를 입력해 주세요:");
        String newCuisineType = scanner.nextLine();
        while(newCuisineType == null || newCuisineType.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newCuisineType = scanner.nextLine();
        }

        String newServiceArea;
        do {
            System.out.println("음식점의 새로운 서비스 지역을 입력해 주세요:");
            newServiceArea = scanner.nextLine();

            if (!newServiceArea.equals("서울") && !newServiceArea.equals("부산") && !newServiceArea.equals("대구") && !newServiceArea.equals("대전") && !newServiceArea.equals("광주") && !newServiceArea.equals("울산")) {
                System.out.println("배달 서비스는 '서울', '부산', '대구', '대전', '광주', '울산'에서만 제공합니다. 다시 작성해주세요.");
                newServiceArea = null;
            }
        } while (newServiceArea == null);

        boolean updateSuccess = updateRestaurantInfo(restaurantId, newName, newAddress, newCuisineType, newServiceArea);
        if (updateSuccess) {
            System.out.println("음식점 정보가 성공적으로 업데이트되었습니다.");
        } else {
            System.out.println("음식점 정보 업데이트에 실패하였습니다.");
        }
    }

    public void manageMenuService() {
        ResultSet resultSet = getRestaurantList();
        List<Integer> restaurantIdList = printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){ // 식당이 없는 경우
            System.out.println("관리하고 있는 식당 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("메뉴를 관리할 식당의 id을 입력해 주세요:");
            restaurantId = scanner.nextInt();
            scanner.nextLine();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        Scanner scanner = new Scanner(System.in);
        System.out.println("메뉴 관리:");
        System.out.println("1. 메뉴 추가");
        System.out.println("2. 메뉴 수정");
        System.out.println("3. 메뉴 삭제");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                System.out.println("추가할 메뉴의 이름을 입력해 주세요:");
                String name = scanner.nextLine();
                while(name == null || name.isEmpty()){
                    System.out.println("아무것도 입력하지 않으셨습니다.");
                    name = scanner.nextLine();
                }

                System.out.println("추가할 메뉴의 가격을 입력해 주세요:");
                double price = scanner.nextDouble();
                scanner.nextLine();  // nextDouble 후에 남은 개행문자 처리
                while(price <= 0){
                    System.out.println("올바른 액수를 입력해주세요.");
                    price = scanner.nextDouble();
                    scanner.nextLine();
                }

                registerMenu(restaurantId, name, price);
                break;

            case 2:
                ResultSet menuSet = getMenuByRestaurantId(restaurantId);
                List<Integer> menuIdList = printMenuList(menuSet);

               if(menuIdList.isEmpty()){
                   System.out.println("등록되어 있는 메뉴가 존재하지 않습니다.");
                   return;
               }

                Integer menuId;
                do {
                    System.out.println("수정할 메뉴의 ID를 입력해 주세요:");
                    menuId = scanner.nextInt();
                    scanner.nextLine();

                    if(!menuIdList.contains(menuId)){
                        System.out.println("선택하신 메뉴 ID는 유효하지 않습니다.");
                    }
                } while(!menuIdList.contains(menuId));

                System.out.println("새로운 이름을 입력해 주세요:");
                String newName = scanner.nextLine();
                while(newName == null || newName.isEmpty()){
                    System.out.println("아무것도 입력하지 않으셨습니다.");
                    newName = scanner.nextLine();
                }

                System.out.println("새로운 가격을 입력해 주세요:");
                double newPrice = scanner.nextDouble();
                scanner.nextLine();
                while(newPrice <= 0){
                    System.out.println("올바른 액수를 입력해주세요.");
                    newPrice = scanner.nextDouble();
                    scanner.nextLine();
                }

                updateMenu(menuId, newName, newPrice);
                System.out.println("메뉴 수정이 성공적으로 수행되었습니다!");

                break;

            case 3:
                menuSet = getMenuByRestaurantId(restaurantId);
                menuIdList = printMenuList(menuSet);

                if(menuIdList.isEmpty()){
                    System.out.println("등록되어 있는 메뉴가 존재하지 않습니다.");
                    return;
                }

                System.out.println("메뉴를 삭제하면 주문 내역이 모두 삭제되니 주의해주세요.");

                do {
                    System.out.println("삭제할 메뉴의 ID를 입력해 주세요:");
                    menuId = scanner.nextInt();
                    scanner.nextLine();

                    if(!menuIdList.contains(menuId)){
                        System.out.println("선택하신 메뉴 ID는 유효하지 않습니다.");
                    }
                } while(!menuIdList.contains(menuId));

                deleteMenu(menuId);
                System.out.println("메뉴 삭제가 성공적으로 수행되었습니다!");

                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }

    public void finishCookingService() {
        ResultSet resultSet = getRestaurantList();
        List<Integer> restaurantIdList = printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){ // 식당이 없는 경우
            System.out.println("관리하고 있는 식당 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("주문완료를 처리할 식당의 id를 입력해 주세요:");
            restaurantId = scanner.nextInt();
            scanner.nextLine();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        ResultSet orderSet= getOrdersByRestaurantId(restaurantId);
        List<Integer> orderIdList = printOrderSet(orderSet); // order 정보들 출력

        if(orderIdList.isEmpty()){ // 주문이 없는 경우
            System.out.println("주문 내역이 존재하지 않습니다.");
            return;
        }

        Integer orderId;
        do {
            System.out.println("처리할 주문의 ID를 입력해 주세요:");
            orderId = scanner.nextInt();
            scanner.nextLine();

            if(!orderIdList.contains(orderId)){
                System.out.println("선택하신 주문 ID는 유효하지 않습니다.");
            }
        } while(!orderIdList.contains(orderId));

        updateOrderStatus(orderId, "cooked");
    }

    public void printOrderHistoryService(){
        ResultSet resultSet = getRestaurantList();
        List<Integer> restaurantIdList = printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){ // 식당이 없는 경우
            System.out.println("관리하고 있는 식당 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("주문 이력을 조회할 음식점의 ID을 입력해 주세요:");
            restaurantId = scanner.nextInt();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        ResultSet orderHistory = getOrderHistory(restaurantId);
        List<Integer> orderIdList = printOrderSet(orderHistory); // order 정보들 출력

        if(orderIdList.isEmpty()){ // 주문이 없는 경우
            System.out.println("주문 내역이 존재하지 않습니다.");
        }
    }

    public void printReviewService(){
        ResultSet resultSet = getRestaurantList();
        List<Integer> restaurantIdList = printRestaurantList(resultSet);

        if(restaurantIdList.isEmpty()){ // 식당이 없는 경우
            System.out.println("관리하고 있는 식당 목록이 존재하지 않습니다.");
            return;
        }

        Integer restaurantId;
        do {
            System.out.println("리뷰를 조회할 음식점의 ID을 입력해 주세요:");
            restaurantId = scanner.nextInt();

            if(!restaurantIdList.contains(restaurantId)){
                System.out.println("선택하신 식당 ID는 유효하지 않습니다.");
            }
        } while(!restaurantIdList.contains(restaurantId));

        ResultSet orderHistory = getOrderHistory(restaurantId);
        List<Integer> orderIdList = printReviewSet(orderHistory);

        if(orderIdList.isEmpty()){ // 주문이 없는 경우
            System.out.println("주문 내역이 존재하지 않습니다.");
        }
    }

    public boolean registerRestaurant(String name, String address, String cuisineType, String serviceArea) {
        String sql = "INSERT INTO Restaurant (name, address, cuisine_type, owner_id, service_area) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, cuisineType);
            preparedStatement.setInt(4, userId);
            preparedStatement.setString(5, serviceArea);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public boolean updateRestaurantInfo(int restaurantId, String name, String address, String cuisineType, String serviceArea) {
        String sql = "UPDATE Restaurant SET name = ?, address = ?, cuisine_type = ?, service_area = ? WHERE restaurant_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, cuisineType);
            preparedStatement.setString(4, serviceArea);
            preparedStatement.setInt(5, restaurantId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e);
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
            handleSQLException(e);
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
            handleSQLException(e);
        }
    }

    public void deleteReviewsWithOrder(int orderId) {
        String sql = "DELETE FROM Review WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteOrdersWithMenu(int menuId) {
        String sql = "SELECT order_id FROM Orders WHERE menu_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, menuId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                // 해당 주문을 참조하는 'Review' 테이블의 레코드를 삭제한다.
                deleteReviewsWithOrder(orderId);
            }

            sql = "DELETE FROM Orders WHERE menu_id = ?";
            preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteMenu(int menuId) {
        // 먼저, 해당 메뉴를 참조하는 'Orders' 테이블의 레코드를 삭제한다
        deleteOrdersWithMenu(menuId);

        String sql = "DELETE FROM Menu WHERE menu_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public ResultSet getRestaurantList() {
        String sql = "SELECT * FROM Restaurant WHERE owner_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
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

                System.out.println("식당 ID: " + restaurantId + ", 식당 이름: "+ name);

                restaurantIdList.add(restaurantId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return restaurantIdList;
    }

    public List<Integer> printMenuList(ResultSet resultSet){
        List<Integer> menuNameList = new ArrayList<>();

        try{
            while(resultSet.next()){
                int menuId = resultSet.getInt("menu_id");
                String name = resultSet.getString("name");

                System.out.println("메뉴 ID: " + menuId + "| 메뉴명: " + name);

                menuNameList.add(menuId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return menuNameList;
    }

    public void updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE Orders SET status = ? WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("처리가 완료되었습니다.");
            } else {
                System.out.println("처리가 실패하였습니다.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public ResultSet getOrdersByRestaurantId(int restaurantId) { // deliveryMatched 상태의 Order들을 얻는다
        String sql = "SELECT * FROM Orders WHERE restaurant_id = ? AND status = 'deliveryMatched'";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public ResultSet getOrderHistory(int restaurantId) {
        String sql = "SELECT * FROM Orders WHERE menu_id IN (SELECT menu_id FROM Menu WHERE restaurant_id = ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public List<Integer> printReviewSet(ResultSet resultSet){
        List<Integer> orderIdList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                int menuId = resultSet.getInt("menu_id");
                String comment = getReviewCommentByOrderId(orderId);

                String menuName = getMenuName(menuId);

                if(comment == null){
                    continue;
                }

                System.out.println("주문 ID: " + orderId + "| 메뉴명: "+ menuName +  "| 리뷰: " + comment);

                orderIdList.add(orderId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return orderIdList;
    }

    public List<Integer> printOrderSet(ResultSet resultSet){
        List<Integer> orderIdList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String orderStatus = resultSet.getString("status");
                int menuId = resultSet.getInt("menu_id");
                Timestamp orderTime = resultSet.getTimestamp("order_time");

                LocalDateTime orderDateTime = orderTime.toLocalDateTime();
                String formattedOrderTime = orderDateTime.toString();
                String menuName = getMenuName(menuId);

                System.out.println("주문 ID: " + orderId + "| 메뉴명: " + menuName + "| 주문 상태: " + orderStatus + "| 주문 시간: " + formattedOrderTime);

                orderIdList.add(orderId);
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return orderIdList;
    }

    public String getMenuName(int menuId) { ////////////////////////// 중복 리팩토링 필요
        String sql = "SELECT name FROM Menu WHERE menu_id = ?";
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

    public String getReviewCommentByOrderId(int orderId) {
        String sql = "SELECT comment FROM Review WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("comment");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public ResultSet getMenuByRestaurantId(int restaurantId) {
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

    private void handleSQLException(SQLException e) {
        System.out.println("SQL 쿼리 실행에서 에러가 발생하였습니다.");
        e.printStackTrace();
    }
}