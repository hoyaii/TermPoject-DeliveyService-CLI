import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestMySQLConnection {
    public static void main(String[] args) {
        try {
            // 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // MySQL 데이터베이스와 연결
            String url = "jdbc:mysql://localhost:3306/dbTermProject";
            String username = "root";
            String password = "0623";
            Connection connection = DriverManager.getConnection(url, username, password);

            // 연결성공 메시지 출력
            System.out.println("Successfully connected to MySQL database!");

            // 연결 종료
            connection.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to MySQL database.");
            e.printStackTrace();
        }
    }
}
