// FoodDeliveryApp.java
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private final Cart cart;

    public Main() {
        this.cart = new Cart();
    }

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        System.out.println("Welcome to the Food Delivery System");

        User user = UserService.authenticateUser(scanner);
        System.out.println("User logged in with ID: " + user.getId());

        displayRandomRestaurants();
        displayRandomFoodItems();
        userMenu(user);
    }

    private static void userMenu(User user) {
        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Search for food or restaurant");
            System.out.println("2. View cart");
            System.out.println("3. Add to cart");
            System.out.println("4. Remove from cart");
            System.out.println("5. Checkout");
            System.out.println("6. Logout");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> search();
                case 2 -> CartService.viewCart(user);
                case 3 -> CartService.addToCart(user, scanner);
                case 4 -> CartService.removeFromCart(user, scanner);
                case 5 -> CartService.checkout(user);
                case 6 -> {
                    System.out.println("Logging out...");
                    running = false;
                    start();
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void displayRandomRestaurants() {
        System.out.println("\nAvailable Restaurants:");
        try (Connection conn = Database.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM restaurants ORDER BY RAND() LIMIT 3");

            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayRandomFoodItems() {
        System.out.println("\nAvailable Food Items:");
        try (Connection conn = Database.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT name FROM food_items ORDER BY RAND() LIMIT 3");

            while (rs.next()) {
                System.out.println("- " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void search() {
        System.out.print("Enter food or restaurant name: ");
        String query = scanner.nextLine();

        System.out.println("\nSearch Results:");
        try (Connection conn = Database.getConnection()) {
            PreparedStatement searchRestaurant = conn.prepareStatement("SELECT name FROM restaurants WHERE name LIKE ?");
            searchRestaurant.setString(1, "%" + query + "%");
            ResultSet rsRestaurants = searchRestaurant.executeQuery();
            while (rsRestaurants.next()) {
                System.out.println("Restaurant: " + rsRestaurants.getString("name"));
            }

            PreparedStatement searchFood = conn.prepareStatement(
                    "SELECT f.name, r.name AS restaurant_name FROM food_items f " +
                            "JOIN restaurants r ON f.restaurant_id = r.id WHERE f.name LIKE ?");
            searchFood.setString(1, "%" + query + "%");
            ResultSet rsFood = searchFood.executeQuery();
            while (rsFood.next()) {
                System.out.println("Food: " + rsFood.getString("name") + " at " + rsFood.getString("restaurant_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
