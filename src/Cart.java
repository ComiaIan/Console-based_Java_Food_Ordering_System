import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    // Method to add an item to the cart
    public void addItem(FoodItem foodItem, int quantity) {
        CartItem cartItem = new CartItem(foodItem, quantity);
        items.add(cartItem);
    }

    // Method to get the quantity of a specific item in the cart from the database
    public int getQuantity(int foodItemId) {
        String query = "SELECT quantity FROM orders WHERE food_item_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, foodItemId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("quantity");
            } else {
                System.out.println("Item not found in orders.");
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Method to remove an item from the cart
    public void removeItem(int foodItemId) {
        items.removeIf(item -> item.getItem().getId() == foodItemId);
    }

    // Method to calculate total price of items in the cart
    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getItem().getPrice() * item.getQuantity())
                .sum();
    }

    // Getters and Setters for cart items
    public List<CartItem> getItems() {
        return items;
    }
}
