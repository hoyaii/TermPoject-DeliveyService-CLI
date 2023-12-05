import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database("jdbc:mysql://localhost:3306/dbTermProject2", "root", "0623");

        ServiceProvider serviceProvider = new ServiceProvider(db);

        while (true) {
            System.out.println("CLI의 민족에 오신 것을 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 로그인");
            System.out.println("2. 회원 가입");
            System.out.println("3. 서비스 종료");

            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 1) {
                // 로그인 진행
                String email = serviceProvider.loginService();

                int userId = serviceProvider.getUserIdByEmail(email);
                String role = serviceProvider.getRoleByEmail(email);

                // 기능 선택
                Runnable handler = null;

                if (role.equals("Customer")) {
                    handler = () -> handleCustomer(serviceProvider, db, userId);
                } else if (role.equals("RestaurantOwner")) {
                    handler = () -> handleRestaurantOwner(serviceProvider, db, userId);
                } else if (role.equals("DeliveryPerson")) {
                    handler = () -> handleDeliveryPerson(serviceProvider, db, userId);
                } else if (role.equals("ServiceProvider")){
                    handler = () -> handleServiceProvider(db);
                }

                // 선택된 핸들러로 계속 처리, 기능 이용을 마치면 다른 기능을 또 이용하도록
                if (handler != null) {
                    handler.run();
                }
            } else if (option == 2) {
                // 회원 가입 진행
                serviceProvider.registerUserService();
            } else if (option == 3) { // 종료 선택
                break;
            }
        }
    }

    public static void handleCustomer(ServiceProvider serviceProvider, Database db, int userId) {
        Customer customer = new Customer(db, userId);
        String userName = serviceProvider.getNameById(userId);

        while(true) {
            System.out.println(userName + " 고객님, 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 음식점 검색과 주문");
            System.out.println("2. 배달 상태 확인");
            System.out.println("3. 리뷰 작성");
            System.out.println("4. 즐겨찾기 조회");
            System.out.println("5. 로그아웃");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 5) {
                break;
            }

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

                case 4:
                    customer.getFavoriteService();
                    break;

                default:
                    System.out.println("잘못된 선택입니다.");
                    break;
            }
        }
    }

    public static void handleRestaurantOwner(ServiceProvider serviceProvider, Database db, int userId) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(db, userId);
        String userName = serviceProvider.getNameById(userId);

        while (true) {
            System.out.println(userName + " 사장님, 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 음식점 정보 등록");
            System.out.println("2. 음식점 정보 업데이트");
            System.out.println("3. 메뉴 관리");
            System.out.println("4. 조리 완료 처리");
            System.out.println("5. 주문 요청/내역 조회");
            System.out.println("6. 리뷰 확인");
            System.out.println("7. 매출/판매량 확인");
            System.out.println("8. 로그아웃");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 8) {
                break;
            }

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
                    restaurantOwner.finishCookingService();
                    break;

                case 5:
                    restaurantOwner.printOrderHistoryService();
                    break;

                case 6:
                    restaurantOwner.printReviewService();
                    break;

                case 7:
                    restaurantOwner.printSalesService();

                default:
                    System.out.println("잘못된 선택입니다.");
                    break;
            }
        }
    }

    public static void handleDeliveryPerson(ServiceProvider serviceProvider, Database db, int userId) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(db, userId);
        String userName = serviceProvider.getNameById(userId);

        while (true) {
            System.out.println(userName + " 배달원님, 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 배달 요청 확인 및 수락");
            System.out.println("2. 배달 완료 처리하기");
            System.out.println("3. 완료한 배달 이력 확인");
            System.out.println("4. 로그아웃");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 4) {
                break;
            }

            switch (option) {
                case 1:
                    deliveryPerson.manageDeliveryRequestService();
                    break;

                case 2:
                    deliveryPerson.finishDeliveryService();
                    break;

                case 3:
                    deliveryPerson.printDeliveryHistoryService();
                    break;

                default:
                    System.out.println("잘못된 선택입니다.");
                    break;
            }
        }
    }

    public static void handleServiceProvider(Database db) {
        ServiceProvider serviceProvider = new ServiceProvider(db);

        while (true) {
            System.out.println("관리자님, 환영합니다! 아래 옵션 중 선택해 주세요:");
            System.out.println("1. 유저 계정 수정");
            System.out.println("2. 유저 계정 삭제");
            System.out.println("3. 로그아웃");

            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            scanner.nextLine();

            if (option == 3) {
                break;
            }

            switch (option) {
                case 1:
                    serviceProvider.updateUserService();
                    break;

                case 2:
                    serviceProvider.deleteUserService();
                    break;

                default:
                    System.out.println("잘못된 선택입니다.");
                    break;
            }
        }
    }
}