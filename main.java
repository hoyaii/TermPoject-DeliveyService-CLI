import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database("jdbc:mysql://localhost:3306/dbTermProject", "root", "0623");

        ServiceProvider serviceProvider = new ServiceProvider(db);

        // 로그인 진행
        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        String role = serviceProvider.login(username, password);
        if (role == null) {
            System.out.println("Invalid username or password.");
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
        // Call customer methods
        // customer.searchRestaurants("Restaurant Name", "Location", "Type");
        // ...
    }

    public static void handleRestaurantOwner(Database db) {
        RestaurantOwner restaurantOwner = new RestaurantOwner(db);
        // Call restaurant owner methods
        // restaurantOwner.registerRestaurant("Restaurant Name", "Location", "Type");
        // ...
    }

    public static void handleDeliveryPerson(Database db) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(db);
        // Call delivery person methods
        // deliveryPerson.updateDeliveryStatus(1, "Delivered");
        // ...
    }

    public static void handleServiceProvider() {
        // Call delivery person methods
        // deliveryPerson.updateDeliveryStatus(1, "Delivered");
        // ...
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