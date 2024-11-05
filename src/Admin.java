import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Admin.java
public class Admin extends User {
    public Admin(int id, String username) {
        super(id, username);
    }

    public void addRestaurant(String name, String location) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO restaurants (name, location) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addFoodItem(int restaurantId, String name, double price) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO food_items (restaurant_id, name, price) VALUES (?, ?, ?)");
            stmt.setInt(1, restaurantId);
            stmt.setString(2, name);
            stmt.setDouble(3, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
