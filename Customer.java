import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customer {
    private Database db;
    private Scanner scanner;

    public Customer(Database db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }

    public void searchRestaurants() {
        System.out.println("Enter restaurant name: ");
        String name = scanner.nextLine();

        System.out.println("Enter restaurant address: ");
        String address = scanner.nextLine();

        System.out.println("Enter cuisine type: ");
        String cuisineType = scanner.nextLine();

        ResultSet resultSet = db.searchRestaurants(name, address, cuisineType);
        try {
            while (resultSet.next()) {
                System.out.println("Restaurant ID: " + resultSet.getInt("restaurant_id"));
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Address: " + resultSet.getString("address"));
                System.out.println("Cuisine Type: " + resultSet.getString("cuisine_type"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error reading from ResultSet.");
            e.printStackTrace();
        }
    }
}