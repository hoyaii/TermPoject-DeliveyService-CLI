import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return false;
        }
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

        System.out.println("배달 서비스를 제공할 지역을 입력해 주세요:");
        String serviceArea = scanner.nextLine();
        while(serviceArea == null || serviceArea.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            serviceArea = scanner.nextLine();
        }

        boolean registerSuccess = registerRestaurant(name, address, cuisineType, serviceArea);
        if (registerSuccess) {
            System.out.println("음식점 정보가 성공적으로 등록되었습니다.");
        } else {
            System.out.println("음식점 정보 등록에 실패하였습니다.");
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
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return false;
        }
    }

    public void updateRestaurantInfoService(){
        System.out.println("정보를 업데이트할 음식점의 이름을 입력해 주세요:");
        String restaurantName = scanner.nextLine();

        System.out.println("업데이트할 음식점의 이름을 입력해 주세요:");
        String newName = scanner.nextLine();
        while(newName == null || newName.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newName = scanner.nextLine();
        }

        System.out.println("업데이트할 음식점의 위치를 입력해 주세요:");
        String newAddress = scanner.nextLine();
        while(newAddress == null || newAddress.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newAddress = scanner.nextLine();
        }

        System.out.println("업데이트할 음식점의 카테고리를 입력해 주세요:");
        String newCuisineType = scanner.nextLine();
        while(newCuisineType == null || newCuisineType.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newCuisineType = scanner.nextLine();
        }

        System.out.println("업데이트할 음식점의 서비스 지역을 입력해 주세요:");
        String newServiceArea = scanner.nextLine();
        while(newServiceArea == null || newServiceArea.isEmpty()){
            System.out.println("아무것도 입력하지 않으셨습니다.");
            newServiceArea = scanner.nextLine();
        }

        Integer restaurantId = getRestaurantIdByName(restaurantName); // restaurantId를 구한다

        boolean updateSuccess = updateRestaurantInfo(restaurantId, newName, newAddress, newCuisineType, newServiceArea);
        if (updateSuccess) {
            System.out.println("음식점 정보가 성공적으로 업데이트되었습니다.");
        } else {
            System.out.println("음식점 정보 업데이트에 실패하였습니다.");
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

    public void deleteMenu(int menuId) {
        String sql = "DELETE FROM Menu WHERE menu_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, menuId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void manageMenuService() {
        System.out.println("추가할 메뉴가 속한 식당의 이름을 입력해 주세요:");
        String restaurantName = scanner.nextLine();
        Integer restaurantId = getRestaurantIdByName(restaurantName);

        if (restaurantId == null) {
            System.out.println("해당 이름의 식당이 없습니다.");
            return;
        }

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
                System.out.println("수정할 메뉴의 ID를 입력해 주세요:");
                int updateId = scanner.nextInt();
                scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
                System.out.println("수정할 메뉴의 새로운 이름을 입력해 주세요:");
                String updateName = scanner.nextLine();
                System.out.println("수정할 메뉴의 새로운 가격을 입력해 주세요:");
                double updatePrice = scanner.nextDouble();
                scanner.nextLine();  // nextDouble 후에 남은 개행문자 처리
                updateMenu(updateId, updateName, updatePrice);
                break;

            case 3:
                System.out.println("삭제할 메뉴의 ID를 입력해 주세요:");
                int deleteId = scanner.nextInt();
                scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
                deleteMenu(deleteId);
                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }

    public List<String> getMenuList(int restaurantId){
        String sql = "SELECT name FROM Menu WHERE restaurant_id = ?";
        List<String> menuNameList = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet  =preparedStatement.executeQuery();
            while(resultSet.next()){
                menuNameList.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }

        return menuNameList;
    }

    public void processOrder(int orderId, String status) {
        String sql = "UPDATE Orders SET order_status = ? WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, orderId);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("주문 처리가 완료되었습니다.");
            } else {
                System.out.println("주문 처리에 실패하였습니다.");
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void manageOrderService() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("처리할 주문의 ID를 입력해 주세요:");
        int orderId = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
        System.out.println("주문의 새로운 상태를 입력해 주세요:");
        String status = scanner.nextLine();

        processOrder(orderId, status);
    }

    public ResultSet getOrderHistory(int restaurantId) {
        String sql = "SELECT * FROM Orders WHERE menu_id IN (SELECT menu_id FROM Menu WHERE restaurant_id = ?)";
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

    public void getOrderHistoryService(){
        System.out.println("주문 이력을 조회할 음식점의 이름을 입력해 주세요:");
        String restaurantName = scanner.nextLine();
        Integer restaurantId = getRestaurantIdByName(restaurantName);
        if (restaurantId != null) {
            ResultSet orderHistory = getOrderHistory(restaurantId);
            try {
                while (orderHistory.next()) {
                    int orderId = orderHistory.getInt("order_id");
                    String orderStatus = orderHistory.getString("order_status");
                    System.out.println("주문 ID: " + orderId + ", 주문 상태: " + orderStatus);
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving order history.");
                e.printStackTrace();
            }
        } else {
            System.out.println("해당 이름의 식당이 없습니다.");
        }
    }

    public Integer getRestaurantIdByName(String name) {
        String sql = "SELECT restaurant_id FROM Restaurant WHERE name = ? AND owner_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, this.userId);
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
}
