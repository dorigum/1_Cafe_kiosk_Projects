package model;

import java.sql.Timestamp;

public class PointHistory {
    private int historyId;
    private long memberId;
    private int amount;
    private String reason;
    private Timestamp createdAt;

    public PointHistory(int historyId, long memberId, int amount, String reason, Timestamp createdAt) {
        this.historyId = historyId;
        this.memberId = memberId;
        this.amount = amount;
        this.reason = reason;
        this.createdAt = createdAt;
    }

    // Getters
    public int getHistoryId() { return historyId; }
    public long getMemberId() { return memberId; }
    public int getAmount() { return amount; }
    public String getReason() { return reason; }
    public Timestamp getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        String sign = amount > 0 ? "+" : "";
        return String.format("[%s] %s%d원 | %s", createdAt, sign, amount, reason);
    }
}