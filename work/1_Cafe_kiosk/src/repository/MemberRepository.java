package repository;

import model.Member;
import model.OrderHistory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {

    // 1. 로그인 확인 (휴대폰 번호와 비밀번호로 조회)
    public Member login(String phone, String password) {
        String sql = "SELECT * FROM MEMBER WHERE phone = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Member(
                        rs.getLong("member_id"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        rs.getInt("point_balance"),
                        rs.getString("role"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("로그인 처리 중 오류: " + e.getMessage());
        }
        return null;
    }

    // 2. 특정 회원의 주문 내역 조회 (JOIN 쿼리 사용)
    public List<OrderHistory> getOrderHistory(long memberId) {
        List<OrderHistory> historyList = new ArrayList<>();
        String sql = "SELECT o.order_id, m.menu_name, oi.quantity, oi.unit_price, o.total_amount, o.ordered_at, o.status " +
                     "FROM ORDERS o " +
                     "JOIN ORDERITEM oi ON o.order_id = oi.order_id " +
                     "JOIN MENU m ON oi.menu_id = m.menu_id " +
                     "WHERE o.member_id = ? " +
                     "ORDER BY o.ordered_at DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    historyList.add(new OrderHistory(
                        rs.getLong("order_id"),
                        rs.getString("menu_name"),
                        rs.getInt("quantity"),
                        rs.getInt("unit_price"),
                        rs.getInt("total_amount"),
                        rs.getTimestamp("ordered_at"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("주문 내역 조회 중 오류: " + e.getMessage());
        }
        return historyList;
    }

    // 3. 모든 회원 조회
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM MEMBER ORDER BY member_id DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                    rs.getLong("member_id"),
                    rs.getString("phone"),
                    rs.getString("password"),
                    rs.getInt("age"),
                    rs.getInt("point_balance"),
                    rs.getString("role"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    // 4. 회원 삭제
    public void deleteMember(long memberId) {
        String sql = "DELETE FROM MEMBER WHERE member_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
