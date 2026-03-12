package repository;

import model.QuickOrder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuickOrderRepository {

	// 퀵오더 저장 (메뉴 + 옵션)
	public boolean saveQuickOrder(long memberId, long menuId, List<Integer> optionIds) {
		String sql = "INSERT INTO QUICK_ORDER (member_id, menu_id) VALUES (?, ?)";
		try (Connection conn = DBUtil.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setLong(1, memberId);
			pstmt.setLong(2, menuId);
			pstmt.executeUpdate();

			// 생성된 quick_order_id 가져오기
			try (ResultSet keys = pstmt.getGeneratedKeys()) {
				if (keys.next()) {
					long quickOrderId = keys.getLong(1);
					// 옵션 저장
					saveQuickOrderOptions(conn, quickOrderId, optionIds);
				}
			}
			return true;
		} catch (SQLException e) {
			System.err.println("퀵오더 저장 실패: " + e.getMessage());
			return false;
		}
	}

	// 퀵오더 옵션 저장
	private void saveQuickOrderOptions(Connection conn, long quickOrderId, List<Integer> optionIds)
			throws SQLException {
		String sql = "INSERT INTO QUICK_ORDER_OPTION (quick_order_id, option_id) VALUES (?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			for (int optionId : optionIds) {
				pstmt.setLong(1, quickOrderId);
				pstmt.setInt(2, optionId);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
		}
	}

	// 회원별 퀵오더 목록 조회
	public List<QuickOrder> getQuickOrdersByMember(long memberId) {
		List<QuickOrder> list = new ArrayList<>();
		String sql = "SELECT * FROM QUICK_ORDER WHERE member_id = ? ORDER BY created_at DESC";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, memberId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					QuickOrder qo = new QuickOrder(rs.getLong("quick_order_id"), rs.getLong("member_id"),
							rs.getLong("menu_id"), rs.getTimestamp("created_at"));
					// 옵션 목록도 함께 조회
					qo.setOptionIds(getOptionIds(rs.getLong("quick_order_id")));
					list.add(qo);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 퀵오더 옵션 ID 목록 조회
	private List<Integer> getOptionIds(long quickOrderId) {
		List<Integer> optionIds = new ArrayList<>();
		String sql = "SELECT option_id FROM QUICK_ORDER_OPTION WHERE quick_order_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, quickOrderId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next())
					optionIds.add(rs.getInt("option_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return optionIds;
	}

	// 퀵오더 삭제
	public boolean deleteQuickOrder(long quickOrderId) {
		String sql = "DELETE FROM QUICK_ORDER WHERE quick_order_id = ?";
		try (Connection conn = DBUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, quickOrderId);
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
