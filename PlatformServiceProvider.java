import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlatformServiceProvider {
    private Database db;

    public PlatformServiceProvider(Database db) {
        this.db = db;
    }

    public void registerUser(String username, String password, String role) {
        String sql = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }

    public void updateUser(int userId, String username, String password, String role) {
        String sql = "UPDATE User SET username = ?, password = ?, role = ? WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }
}
