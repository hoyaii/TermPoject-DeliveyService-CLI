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

    public void manageMenu() {
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
                String addName = scanner.nextLine();
                System.out.println("추가할 메뉴의 가격을 입력해 주세요:");
                double addPrice = scanner.nextDouble();
                scanner.nextLine();  // nextDouble 후에 남은 개행문자 처리
                System.out.println("추가할 메뉴가 속한 식당의 이름을 입력해 주세요:");
                String restaurantName = scanner.nextLine();
                Integer restaurantId = getRestaurantIdByName(restaurantName);
                if (restaurantId != null) {
                    registerMenu(restaurantId, addName, addPrice);
                } else {
                    System.out.println("해당 이름의 식당이 없습니다.");
                }
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
