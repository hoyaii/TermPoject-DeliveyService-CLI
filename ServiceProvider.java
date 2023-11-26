import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceProvider {
    private Database db;
    private Scanner scanner;

    // 이메일 패턴
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    // 비밀번호 패턴 (영문과 숫자 포함 8자 이상)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");

    public ServiceProvider(Database db) {
        this.db = db;
        this.scanner = new Scanner(System.in);
    }

    public String loginService(){
        String email;
        do {
            System.out.println("이메일을 입력하세요:");
            email = scanner.nextLine();

            if (!isValidEmail(email)) {
                System.out.println("이메일 형식이 올바르지 않습니다.");
                email = null;
            }
            if (getUserIdByEmail(email) == 0) {
                System.out.println("가입되지 않은 계정입니다. 회원가입 후 다시 시도해주세요.");
                email = null;
            }

        } while (email == null);

        String password;
        do {
            System.out.println("비밀번호를 입력하세요:");
            password = scanner.nextLine();

            if (!isValidPassword(password)) {
                System.out.println("비밀번호 형식이 올바르지 않습니다.");
                password = null;
            }
            if(!login(email, password)){
                System.out.println("비밀번호가 잘못되었습니다. 다시 시도해주세요.");
                password = null;
            }
        } while (password == null);

        System.out.println("로그인 성공하였습니다!");
        return email;
    }

    public void registerUserService(){
        String email = null;
        do {
            System.out.println("이메일을 입력해 주세요:");
            email = scanner.nextLine();

            if (!isValidEmail(email)) {
                System.out.println("올바른 이메일 형식을 입력해주세요.");
                email = null;
            }

            if (getUserIdByEmail(email) != 0) { // 이메일 중복 확인
                System.out.println("이미 사용 중인 이메일입니다. 다른 이메일을 입력해주세요.");
                email = null;
            }
        } while (email == null);

        String username;
        do {
            System.out.println("이름을 입력해 주세요:");
            username = scanner.nextLine();
        } while (username == null || username.isEmpty());

        String password;
        do {
            System.out.println("비밀번호를 입력해 주세요:");
            password = scanner.nextLine();

            if (!isValidPassword(password)) {
                System.out.println("8자 이상의 영문과 숫자를 입력해주세요.");
                password = null;
            }
        } while (password == null);

        String role;
        do {
            System.out.println("역할을 입력해 주세요:");
            role = scanner.nextLine();

            if (!role.equals("Customer") && !role.equals("RestaurantOwner") && !role.equals("DeliveryPerson") && !role.equals("ServiceProvider")) {
                System.out.println("역할은 'Customer', 'RestaurantOwner', 'DeliveryPerson', 'ServiceProvider' 중 하나여야 합니다.");
                role = null;
            }
        } while (role == null);

        String phoneNumber;
        do {
            System.out.println("휴대폰 번호를 입력해 주세요:");
            phoneNumber = scanner.nextLine();
        } while (phoneNumber == null || phoneNumber.isEmpty());

        String address;
        do {
            System.out.println("주소를 입력해 주세요:");
            address = scanner.nextLine();
        } while (address == null || address.isEmpty());

        registerUser(email, username, password, role, phoneNumber, address);

        // 배달원의 경우 추가 정보 입력
        int userId = getUserIdByEmail(email);

        if (role.equals("DeliveryPerson")) {
            System.out.println("이어서 다음 정보를 입력해주세요.");

            String serviceArea;
            do {
                System.out.println("배달 지역을 입력해 주세요:");
                serviceArea = scanner.nextLine();

                if (!serviceArea.equals("서울") && !serviceArea.equals("부산") && !serviceArea.equals("대구") && !serviceArea.equals("대전") && !serviceArea.equals("광주") && !serviceArea.equals("울산")) {
                    System.out.println("배달 서비스는 '서울', '부산', '대구', '대전', '광주', '울산'에서만 제공합니다. 다시 작성해주세요.");
                    serviceArea = null;
                }
            } while (serviceArea == null);

            addDeliveryPersonInfo(userId, serviceArea);
        }
    }

    public void updateUserService(){
        String email;
        do {
            System.out.println("수정할 유저의 이메일을 입력해 주세요:");
            email = scanner.nextLine();

            if (!isValidEmail(email)) {
                System.out.println("이메일 형식이 올바르지 않습니다.");
                email = null;
            }

            if (getUserIdByEmail(email) == 0) {
                System.out.println("가입되지 않은 계정입니다. 회원가입 후 다시 시도해주세요.");
                email = null;
            }

        } while (email == null);

        int userId = getUserIdByEmail(email);

        String newName;
        do {
            System.out.println("새로운 이름을 입력해 주세요:");
            newName = scanner.nextLine();
        } while (newName == null || newName.isEmpty());

        String newPassword;
        do {
            System.out.println("새로운 비밀번호를 입력해 주세요:");
            newPassword = scanner.nextLine();

            if (!isValidPassword(newPassword)) {
                System.out.println("8자 이상의 영문과 숫자를 입력해주세요.");
                newPassword = null;
            }
        } while (newPassword == null);

        String newRole;
        do {
            System.out.println("새로운 역할을 입력해 주세요:");
            newRole = scanner.nextLine();

            if (!newRole.equals("Customer") && !newRole.equals("RestaurantOwner") && !newRole.equals("DeliveryPerson") && !newRole.equals("ServiceProvider")) {
                System.out.println("역할은 'Customer', 'RestaurantOwner', 'DeliveryPerson', 'ServiceProvider' 중 하나여야 합니다.");
                newRole = null;
            }
        } while (newRole == null);

        updateUser(userId, newName, newPassword, newRole);
    }

    public void deleteUserService(){
        String email;
        do {
            System.out.println("삭제할 유저의 이메일을 입력해 주세요:");
            email = scanner.nextLine();

            if (!isValidEmail(email)) {
                System.out.println("이메일 형식이 올바르지 않습니다.");
                email = null;
            }

            if (getUserIdByEmail(email) == 0) {
                System.out.println("가입되지 않은 계정입니다. 회원가입 후 다시 시도해주세요.");
                email = null;
            }

        } while (email == null);

        int userId = getUserIdByEmail(email);

        deleteUser(userId);
    }

    public boolean login(String email, String password) {
        String sql = "SELECT user_id FROM User WHERE email = ? AND password = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return false;
        }
    }

    public void registerUser(String email, String username, String password, String role, String phoneNumber, String address) {
        String sql = "INSERT INTO User (email, username, password, role, phone_number, address) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.setString(4, role);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, address);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void updateUser(int userId, String username, String password, String role) { // email은 unique해야 해서 수정이 불가능하다
        String sql = "UPDATE User SET username = ?, password = ?, role = ? WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, role);
            preparedStatement.setInt(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM User WHERE email = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return 0;
    }

    public String getRoleByEmail(String email) {
        String sql = "SELECT role FROM User WHERE email = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("role");
            } else {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return null;
        }
    }

    public void addDeliveryPersonInfo(int userId, String serviceArea) {
        String sql = "UPDATE User SET service_area = ?, status = 'free' WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = this.db.connection.prepareStatement(sql);
            preparedStatement.setString(1, serviceArea);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    private void handleSQLException(SQLException e) {
        System.out.println("SQL 쿼리 실행에서 에러가 발생하였습니다.");
        e.printStackTrace();
    }
}