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
        System.out.println("이메일을 입력하세요:");
        String email = scanner.nextLine();

        System.out.println("비밀번호를 입력하세요:");
        String password = scanner.nextLine();

        Integer userId = serviceProvider.getUserIdByEmail(email);
        String role = serviceProvider.login(email, password);
        if (role == null) {
            System.out.println("사용자 이름 또는 비밀번호가 잘못되었습니다.");

        } else if (role.equals("Customer")) {
            handleCustomer(db);

        } else if (role.equals("RestaurantOwner")) {
            handleRestaurantOwner(db, userId);

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

            case 3:
                System.out.println("배달 상태를 확인하고 싶은 주문의 ID를 입력해 주세요:");
                int orderId = scanner.nextInt();
                scanner.nextLine();  // nextInt 후에 남은 개행문자 처리

                String deliveryStatus = customer.getDeliveryStatus(orderId);
                if (deliveryStatus != null) {
                    System.out.println("배달 상태: " + deliveryStatus);
                } else {
                    System.out.println("주문 ID가 잘못되었거나, 배달 상태를 확인할 수 없습니다.");
                }
                break;
        }
    }

    public static void handleRestaurantOwner(Database db, Integer userId) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(db, userId);

        System.out.println("사장님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 음식점 정보 등록");
        System.out.println("2. 음식점 정보 업데이트");
        System.out.println("3. 메뉴 관리");
        System.out.println("4. 주문 처리");
        System.out.println("5. 주문 이력 조회");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                System.out.println("등록할 음식점의 이름을 입력해 주세요:");
                String registerName = scanner.nextLine();
                System.out.println("등록할 음식점의 위치를 입력해 주세요:");
                String registerLocation = scanner.nextLine();
                System.out.println("등록할 음식점의 음식 종류를 입력해 주세요:");
                String registerType = scanner.nextLine();

                boolean registerSuccess = restaurantOwner.registerRestaurant(registerName, registerLocation, registerType);
                if (registerSuccess) {
                    System.out.println("음식점 정보가 성공적으로 등록되었습니다.");
                } else {
                    System.out.println("음식점 정보 등록에 실패하였습니다.");
                }
                break;

            case 2:
                System.out.println("기존의 음식점의 이름을 입력해 주세요:");
                String restaurantName = scanner.nextLine();
                System.out.println("업데이트할 음식점의 이름을 입력해 주세요:");
                String updateName = scanner.nextLine();
                System.out.println("업데이트할 음식점의 위치를 입력해 주세요:");
                String updateLocation = scanner.nextLine();
                System.out.println("업데이트할 음식점의 음식 종류를 입력해 주세요:");
                String updateType = scanner.nextLine();

                Integer restaurantId = restaurantOwner.getRestaurantIdByName(restaurantName); // restaurantId를 구한다

                boolean updateSuccess = restaurantOwner.updateRestaurantInfo(restaurantId, updateName, updateLocation, updateType);
                if (updateSuccess) {
                    System.out.println("음식점 정보가 성공적으로 업데이트되었습니다.");
                } else {
                    System.out.println("음식점 정보 업데이트에 실패하였습니다.");
                }
                break;
                
            case 3:
                restaurantOwner.manageMenu();
                break;
        }
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