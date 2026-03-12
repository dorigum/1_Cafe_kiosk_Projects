package model;

import java.util.Date;
import java.util.List;

public class QuickOrder {
	private long quickOrderId;
	private long memberId;
	private long menuId;
	private Date createdAt;
	private List<Integer> optionIds; // 저장된 옵션 목록

	// DB 조회용
	public QuickOrder(long quickOrderId, long memberId, long menuId, Date createdAt) {
		this.quickOrderId = quickOrderId;
		this.memberId = memberId;
		this.menuId = menuId;
		this.createdAt = createdAt;
	}

	// 등록용
	public QuickOrder(long memberId, long menuId, List<Integer> optionIds) {
		this.memberId = memberId;
		this.menuId = menuId;
		this.optionIds = optionIds;
	}

	public long getQuickOrderId() {
		return quickOrderId;
	}

	public long getMemberId() {
		return memberId;
	}

	public long getMenuId() {
		return menuId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public List<Integer> getOptionIds() {
		return optionIds;
	}

	public void setOptionIds(List<Integer> optionIds) {
		this.optionIds = optionIds;
	}

	@Override
	public String toString() {
		return String.format("퀵오더 번호: %d | 메뉴ID: %d | 옵션: %s", quickOrderId, menuId, optionIds);
	}
}