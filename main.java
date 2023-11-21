import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Database db = new Database("jdbc:mysql://localhost:3306/dbTermProject", "root", "0623");

        System.out.println("Enter username: ");
        String username = scanner.nextLine();

        System.out.println("Enter password: ");
        String password = scanner.nextLine();

        if (db.checkLogin(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Login failed.");
        }

        /*
        Customer customer = new Customer(db);
        customer.searchRestaurants();
         */

        db.close();
        scanner.close();
    }
}
