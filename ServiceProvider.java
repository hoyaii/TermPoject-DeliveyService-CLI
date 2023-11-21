import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceProvider {
    private Database db;

    public ServiceProvider(Database db) {
        this.db = db;
    }

    public String login(String username, String password) {
        String sql = "SELECT role FROM User WHERE username = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
            return null;
        }
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

    public void deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error executing SQL query.");
            e.printStackTrace();
        }
    }
}
