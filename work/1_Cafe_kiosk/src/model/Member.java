package model;

import java.util.Date;

public class Member {
    private long memberId;
    private String phone;
    private String password;
    private int age;
    private int pointBalance;
    private String role;
    private Date createdAt;

    public Member(long memberId, String phone, String password, int age, int pointBalance, String role, Date createdAt) {
        this.memberId = memberId;
        this.phone = phone;
        this.password = password;
        this.age = age;
        this.pointBalance = pointBalance;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getters
    public long getMemberId() { return memberId; }
    public String getPhone() { return phone; }
    public String getPassword() { return password; }
    public int getPointBalance() { return pointBalance; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return String.format("[%s]님 (포인트: %d원, 등급: %s)", phone, pointBalance, role);
    }
}
