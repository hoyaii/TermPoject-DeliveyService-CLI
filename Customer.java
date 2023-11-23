import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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

    public void searchRestaurantsService(){
        System.out.println("검색하실 음식점의 이름을 입력해 주세요:");
        String name = scanner.nextLine();
        System.out.println("검색하실 음식점의 위치를 입력해 주세요:");
        String location = scanner.nextLine();
        System.out.println("검색하실 음식점의 음식 종류를 입력해 주세요:");
        String type = scanner.nextLine();

        ResultSet resultSet = searchRestaurants(name, location, type);
        try {
            while (resultSet.next()) {
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

    public boolean orderMenu(int restaurantId, int menuId) {
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

    public void menuService(){
        System.out.println("주문하실 음식점의 ID를 입력해 주세요:");
        int restaurantId = scanner.nextInt();
        scanner.nextLine();

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

        boolean orderSuccess = orderMenu(restaurantId, menuId);
        if (orderSuccess) {
            System.out.println("주문이 성공적으로 완료되었습니다.");
        } else {
            System.out.println("주문에 실패하였습니다.");
        }
    }

    public String getDeliveryStatus(int orderId) {
        String sql = "SELECT delivery_status FROM Orders WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("delivery_status");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
    }

    public void getDeliveryStatusService(){
        System.out.println("배달 상태를 확인하고 싶은 주문의 ID를 입력해 주세요:");
        int orderId = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리

        String deliveryStatus = getDeliveryStatus(orderId);
        if (deliveryStatus != null) {
            System.out.println("배달 상태: " + deliveryStatus);
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

    public void writeReviewService(){
        System.out.println("리뷰를 작성할 음식점의 이름을 입력해 주세요:");
        String restaurantName = scanner.nextLine();
        System.out.println("리뷰의 별점을 입력해 주세요:");
        int rating = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리
        System.out.println("리뷰 내용을 입력해 주세요:");
        String reviewContent = scanner.nextLine();

        writeReview(restaurantName, rating, reviewContent);
    }
}