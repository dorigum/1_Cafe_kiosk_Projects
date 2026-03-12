package model;

import java.util.Date;

public class Order {
    private long orderId;
    private long memberId;
    private int totalAmount;
    private int pointUsed;
    private int pointEarned;
    private String status;
    private Date orderDate;

    public Order(long orderId, long memberId, int totalAmount, int pointUsed, int pointEarned, String status, Date orderDate) {
        this.orderId = orderId;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.pointUsed = pointUsed;
        this.pointEarned = pointEarned;
        this.status = status;
        this.orderDate = orderDate;
    }

    public long getOrderId() { return orderId; }
    public long getMemberId() { return memberId; }
    public int getTotalAmount() { return totalAmount; }
    public int getPointUsed() { return pointUsed; }
    public int getPointEarned() { return pointEarned; }
    public String getStatus() { return status; }
    public Date getOrderDate() { return orderDate; }

    @Override
    public String toString() {
        String mark = "[ ]";
        if ("CANCELLED".equals(status)) {
            mark = "[X]";
        } else if ("COMPLETED".equals(status)) {
            mark = "[V]";
        }
        
        String memberInfo = (memberId > 0) ? String.valueOf(memberId) : "비회원";
        
        return String.format("%s 주문번호: %d | 주문자(ID): %s | 총액: %,d원 | 상태: %-10s | 주문일시: %s", 
                mark, orderId, memberInfo, totalAmount, status, orderDate);
    }
}
