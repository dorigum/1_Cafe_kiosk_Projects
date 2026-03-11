package model;

import java.util.Date;

public class OrderHistory {
    private long orderId;
    private String menuName;
    private int quantity;
    private int unitPrice;
    private int totalAmount;
    private Date orderedAt;
    private String status;

    public OrderHistory(long orderId, String menuName, int quantity, int unitPrice, int totalAmount, Date orderedAt, String status) {
        this.orderId = orderId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.orderedAt = orderedAt;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("주문번호: %d | %-12s | %d개 | 단가: %d | 총액: %d | 상태: %s | 일시: %s", 
                orderId, menuName, quantity, unitPrice, totalAmount, status, orderedAt);
    }
}
