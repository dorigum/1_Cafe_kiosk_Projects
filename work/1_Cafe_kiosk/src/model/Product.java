package model;

import java.util.Date;

public class Product {
    private int id;               // menu_id (DB에서 자동으로 생성됨)
    private int categoryId;       // category_id
    private String name;          // menu_name
    private int price;            // price
    private String description;   // description
    private boolean isAvailable;  // is_available (품절 여부)
    private Date createdAt;       // created_at

    // 1. 상품 등록용 생성자 (id는 DB에서 자동 생성되므로 제외)
    public Product(int categoryId, String name, int price, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isAvailable = true; // 기본값: 판매 가능
    }

    // 2. DB 조회용 전체 생성자
    public Product(int id, int categoryId, String name, int price, String description, boolean isAvailable, Date createdAt) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public boolean isAvailable() { return isAvailable; }

    @Override
    public String toString() {
        return String.format("[%d][Cat:%d] %-15s | %6d원 | 가능: %s", 
                id, categoryId, name, price, isAvailable ? "YES" : "NO");
    }
}
