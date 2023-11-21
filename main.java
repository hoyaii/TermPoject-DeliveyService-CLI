import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database("jdbc:mysql://localhost:3306/dbTermProject", "root", "0623");

        ServiceProvider serviceProvider = new ServiceProvider(db);

        // 로그인 진행
        System.out.println("서비스에 오신 것을 환영합니다! 로그인해 주세요.");
        System.out.println("사용자 이름을 입력하세요:");
        String username = scanner.nextLine();

        System.out.println("비밀번호를 입력하세요:");
        String password = scanner.nextLine();

        String role = serviceProvider.login(username, password);
        if (role == null) {
            System.out.println("사용자 이름 또는 비밀번호가 잘못되었습니다.");

        } else if (role.equals("Customer")) {
            handleCustomer(db);

        } else if (role.equals("RestaurantOwner")) {
            handleRestaurantOwner(db);

        } else if (role.equals("DeliveryPerson")) {
            handleDeliveryPerson(db);

        } else if (role.equals("ServiceProvider")){
            handleServiceProvider();
        }

        db.close();
        scanner.close();
    }

    public static void handleCustomer(Database db) {
        Customer customer = new Customer(db);

        System.out.println("고객님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 음식점 검색");
        System.out.println("2. 메뉴 선택 및 주문");
        System.out.println("3. 배달 상태 확인");
        System.out.println("4. 리뷰 작성");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();  // nextInt 후에 남은 개행문자 처리

        switch (option) {
            case 1:
                System.out.println("검색하실 음식점의 이름을 입력해 주세요:");
                String name = scanner.nextLine();
                System.out.println("검색하실 음식점의 위치를 입력해 주세요:");
                String location = scanner.nextLine();
                System.out.println("검색하실 음식점의 음식 종류를 입력해 주세요:");
                String type = scanner.nextLine();

                ResultSet resultSet = customer.searchRestaurants(name, location, type);
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
                break;
            // 추가적인 기능 구현

            case 2:
                System.out.println("주문하실 음식점의 ID를 입력해 주세요:");
                int restaurantId = scanner.nextInt();
                scanner.nextLine();  // nextInt 후에 남은 개행문자 처리

                ResultSet menuResultSet = customer.getMenu(restaurantId);
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
                scanner.nextLine();  // nextInt 후에 남은 개행문자 처리

                boolean orderSuccess = customer.orderMenu(restaurantId, menuId);
                if (orderSuccess) {
                    System.out.println("주문이 성공적으로 완료되었습니다.");
                } else {
                    System.out.println("주문에 실패하였습니다.");
                }
                break;
        }
    }

    public static void handleRestaurantOwner(Database db) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(db);

        System.out.println("사장님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 음식점 정보 등록 및 업데이트");
        System.out.println("2. 메뉴 관리");
        System.out.println("3. 주문 처리");
        System.out.println("4. 주문 이력 조회");
    }

    public static void handleDeliveryPerson(Database db) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(db);

        System.out.println("배달원님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 배달 리스트 확인");
        System.out.println("2. 배달 상태 업데이트");
        System.out.println("3. 배달 이력 확인");
    }

    public static void handleServiceProvider() {
        System.out.println("관리자님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 유저 계정 등록");
        System.out.println("2. 유저 계정 수정");
        System.out.println("3. 유저 계정 삭제");
    }
}



/*
        Customer customer = new Customer(db);
        customer.searchRestaurants();
         */

        /* 배달 상태 업데이트
        DeliveryPerson deliveryPerson = new DeliveryPerson(db);
        deliveryPerson.updateDeliveryStatus(1, "Delivered");
         */

        /* 배달 이력 조회
        DeliveryPerson deliveryPerson = new DeliveryPerson(db);
        ResultSet resultSet = deliveryPerson.getDeliveryHistory(1);
        try {
            while (resultSet.next()) {
                System.out.println("Order ID: " + resultSet.getInt("order_id"));
                System.out.println("Customer ID: " + resultSet.getInt("customer_id"));
                System.out.println("Menu ID: " + resultSet.getInt("menu_id"));
                System.out.println("Order Status: " + resultSet.getString("order_status"));
                System.out.println("Order Time: " + resultSet.getTimestamp("order_time"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from ResultSet.");
            e.printStackTrace();
        }
         */

        /*
        계정 등록/수정/삭제
        platformProvider.registerUser("newUser", "password", "Customer");
        platformProvider.updateUser(1, "updatedUser", "newPassword", "Customer");
        platformProvider.deleteUser(1);
         */