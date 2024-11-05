import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserService {
    public static User authenticateUser(Scanner scanner) {
        System.out.println("1. Login\n2. Register\nChooce an option:  ");
        int choice = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (choice == 1) {
            User user = User.login(username, password);
            if (user != null) {
                System.out.println("Login successful. Welcome, " + username + "!");
                return user;
            } else {
                System.out.println("Invalid credentials.");
                return authenticateUser(scanner);
            }
        } else if (choice == 2) {
            if (User.register(username, password)) {
                System.out.println("Registration successful. You can now log in.");
                return authenticateUser(scanner);
            } else {
                System.out.println("Username already exists. Try a different one.");
                return authenticateUser(scanner);
            }
        } else {
            System.out.println("Invalid choice.");
            return authenticateUser(scanner);
        }
    }

    public static boolean doesUserExist(int userId) {
        try (Connection conn = Database.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
