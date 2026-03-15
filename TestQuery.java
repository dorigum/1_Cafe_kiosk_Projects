import java.sql.*;

public class TestQuery {
    public static void main(String[] args) {
        // Updated with the host and credentials provided by the user
        String url = "jdbc:mysql://54.180.25.109:3306/kiosk?serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
        String user = "root";
        String password = "admin";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("--- Current Member Points (Remote DB: 54.180.25.109) ---");
            String sql = "SELECT member_id, phone, point_balance, role FROM MEMBER ORDER BY member_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                System.out.printf("ID: %4d | Phone: %s | Points: %7d | Role: %s%n",
                        rs.getLong("member_id"),
                        rs.getString("phone"),
                        rs.getInt("point_balance"),
                        rs.getString("role"));
            }
            System.out.println("-------------------------------------------------------");
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Error connecting to remote database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}