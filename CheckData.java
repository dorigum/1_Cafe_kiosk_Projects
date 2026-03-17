import java.sql.*;

public class CheckData {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/kiosk?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String user = "root";
        String pass = "1234"; // 환경에 맞게 수정 필요

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("--- [1] 옵션 그룹 중복 확인 ---");
            String ogSql = "SELECT group_name, COUNT(*) as cnt FROM OPTION_GROUP GROUP BY group_name HAVING cnt > 1";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(ogSql)) {
                while (rs.next()) {
                    System.out.println("중복 그룹명: " + rs.getString("group_name") + " (" + rs.getInt("cnt") + "개)");
                }
            }

            System.out.println("\n--- [2] 010-5612-5447 회원 정보 확인 ---");
            String memSql = "SELECT member_id, phone, point_balance FROM MEMBER WHERE phone = '010-5612-5447'";
            long memberId = -1;
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(memSql)) {
                if (rs.next()) {
                    memberId = rs.getLong("member_id");
                    System.out.println("ID: " + memberId + ", 폰번호: " + rs.getString("phone") + ", 잔액: " + rs.getInt("point_balance"));
                } else {
                    System.out.println("해당 회원이 존재하지 않습니다.");
                }
            }

            if (memberId != -1) {
                System.out.println("\n--- [3] 해당 회원의 포인트 내역 확인 ---");
                String phSql = "SELECT * FROM POINT_HISTORY WHERE member_id = " + memberId;
                try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(phSql)) {
                    boolean hasHistory = false;
                    while (rs.next()) {
                        hasHistory = true;
                        System.out.println("ID: " + rs.getInt("history_id") + ", 금액: " + rs.getInt("amount") + ", 사유: " + rs.getString("reason") + ", 일시: " + rs.getTimestamp("created_at"));
                    }
                    if (!hasHistory) System.out.println("내역이 전혀 없습니다.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
