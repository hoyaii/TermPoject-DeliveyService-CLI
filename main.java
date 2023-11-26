import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database("jdbc:mysql://localhost:3306/dbTermProject", "root", "0623");

        ServiceProvider serviceProvider = new ServiceProvider(db);

        while (true) {
            System.out.println("서비스에 오신 것을 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 로그인");
            System.out.println("2. 회원 가입");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                // 로그인 진행
                String email = serviceProvider.loginService();

                int userId = serviceProvider.getUserIdByEmail(email);
                String role = serviceProvider.getRoleByEmail(email);

                // 기능 선택
                if (role.equals("Customer")) {
                    handleCustomer(db, userId);
                    break;
                } else if (role.equals("RestaurantOwner")) {
                    handleRestaurantOwner(db, userId);
                    break;
                } else if (role.equals("DeliveryPerson")) {
                    handleDeliveryPerson(db, userId);
                    break;
                } else if (role.equals("ServiceProvider")){
                    handleServiceProvider(db);
                    break;
                }
            } else if (option == 2) {
                // 회원 가입 진행
                serviceProvider.registerUserService();
            }
        }

        db.close();
        scanner.close();
    }

    public static void handleCustomer(Database db, int userId) {
        Customer customer = new Customer(db, userId);

        System.out.println("고객님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 음식점 검색과 주문");
        System.out.println("2. 배달 상태 확인");
        System.out.println("3. 리뷰 작성");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                customer.searchRestaurantsService();
                break;

            case 2:
                customer.getDeliveryStatusService();
                break;

            case 3:
                customer.writeReviewService();
                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }

    public static void handleRestaurantOwner(Database db, int userId) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(db, userId);

        System.out.println("사장님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 음식점 정보 등록");
        System.out.println("2. 음식점 정보 업데이트");
        System.out.println("3. 메뉴 관리");
        System.out.println("4. 조리 완료 처리하기");
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
                restaurantOwner.finishCookedService();
                break;

            case 5:
                restaurantOwner.getOrderHistoryService();
                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }

    public static void handleDeliveryPerson(Database db, int userId) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(db, userId);

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        System.out.println("배달원님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 배달 요청 확인 및 수락");
        System.out.println("2. 배달 완료 처리하기");
        System.out.println("3. 완료한 배달 이력 확인");

        switch (option) {
            case 1:
                deliveryPerson.getDeliveryRequestService();
                break;

            case 2:
                deliveryPerson.finishDeliveryService();
                break;

            case 3:
                deliveryPerson.getDeliveryHistoryService();
                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }

    public static void handleServiceProvider(Database db) {
        ServiceProvider serviceProvider = new ServiceProvider(db);

        System.out.println("관리자님, 환영합니다! 아래 옵션 중 선택해 주세요:");
        System.out.println("1. 유저 계정 등록");
        System.out.println("2. 유저 계정 수정");
        System.out.println("3. 유저 계정 삭제");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1:
                serviceProvider.registerUserService();
                break;

            case 2:
                serviceProvider.updateUserService();
                break;

            case 3:
                serviceProvider.deleteUserService();
                break;

            default:
                System.out.println("잘못된 선택입니다.");
                break;
        }
    }
}