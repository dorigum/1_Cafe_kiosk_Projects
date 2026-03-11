package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    // 1. 총 매출액 조회
    public int getTotalSales() {
        String sql = "SELECT SUM(total_amount) FROM ORDERS WHERE status = 'COMPLETED'";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 카테고리별 매출 통계
    public Map<String, Integer> getSalesByCategory() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        String sql = "SELECT c.category_name, SUM(oi.unit_price * oi.quantity) as sales " +
                     "FROM ORDERITEM oi " +
                     "JOIN MENU m ON oi.menu_id = m.menu_id " +
                     "JOIN CATEGORY c ON m.category_id = c.category_id " +
                     "JOIN ORDERS o ON oi.order_id = o.order_id " +
                     "WHERE o.status = 'COMPLETED' " +
                     "GROUP BY c.category_name " +
                     "ORDER BY sales DESC";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("category_name"), rs.getInt("sales"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // 3. 인기 메뉴 Top 3 조회
    public List<String> getTopSellingMenus() {
        List<String> topMenus = new ArrayList<>();
        String sql = "SELECT m.menu_name, SUM(oi.quantity) as total_qty " +
                     "FROM ORDERITEM oi " +
                     "JOIN MENU m ON oi.menu_id = m.menu_id " +
                     "JOIN ORDERS o ON oi.order_id = o.order_id " +
                     "WHERE o.status = 'COMPLETED' " +
                     "GROUP BY m.menu_name " +
                     "ORDER BY total_qty DESC LIMIT 3";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                topMenus.add(String.format("%-15s | %d개 판매", rs.getString("menu_name"), rs.getInt("total_qty")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topMenus;
    }

    // 4. 일별 매출 데이터 추출 (최근 7일)
    public Map<String, Integer> getDailySales() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        String sql = "SELECT DATE(ordered_at) as order_date, SUM(total_amount) as daily_total " +
                     "FROM ORDERS WHERE status = 'COMPLETED' " +
                     "GROUP BY DATE(ordered_at) " +
                     "ORDER BY order_date ASC LIMIT 7";
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("order_date"), rs.getInt("daily_total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    // 5. 주문 취소 (상태 업데이트)
    public boolean cancelOrder(long orderId) {
        String sql = "UPDATE ORDERS SET status = 'CANCELLED' WHERE order_id = ? AND status = 'COMPLETED'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, orderId);
            int affected = pstmt.executeUpdate();
            return affected > 0; // 업데이트된 행이 있으면 성공
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. 모든 주문 목록 조회 (취소용)
    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        String sql = "SELECT order_id, total_amount, status, ordered_at FROM ORDERS ORDER BY ordered_at DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                orders.add(String.format("ID: %d | 금액: %d | 상태: %s | 일시: %s", 
                        rs.getLong("order_id"), rs.getInt("total_amount"), 
                        rs.getString("status"), rs.getTimestamp("ordered_at")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
