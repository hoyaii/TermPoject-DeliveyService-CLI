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
                customer.searchRestaurantsService();
                break;

            case 2:
                customer.menuService();
                break;

            case 3:
                customer.getDeliveryStatusService();
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
                restaurantOwner.registerRestaurantService();
                break;

            case 2:
                restaurantOwner.updateRestaurantInfoService();
                break;

            case 3:
                restaurantOwner.manageMenuService();
                break;

            case 4:
                restaurantOwner.manageOrderService();
                break;

            case 5:
                restaurantOwner.getOrderHistoryService();
                break;

            default:
                System.out.println("잘못된 선택입니다.");
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