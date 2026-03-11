-- 1. 데이터베이스 생성 및 선택
CREATE DATABASE IF NOT EXISTS cafe_kiosk;
USE cafe_kiosk;

-- 2. 기존 테이블 삭제 (초기화용 - 필요 시 사용)
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS ORDERITEMOPTION, QUICKORDEROPTION, WISHLIST, ORDERITEM, QUICKORDER, ORDERS, MENUOPTIONGROUP, OPTION, OPTIONGROUP, MENU, CATEGORY, MEMBER;
SET FOREIGN_KEY_CHECKS = 1;

-- 3. CATEGORY 테이블
CREATE TABLE CATEGORY (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(30) NOT NULL
);

-- 4. MENU 테이블
CREATE TABLE MENU (
    menu_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    menu_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    description TEXT,
    is_available TINYINT(1) DEFAULT 1, -- 1: 판매가능, 0: 품절
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id)
);

-- 5. OPTIONGROUP 테이블 (사이즈, 온도 등)
CREATE TABLE OPTIONGROUP (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(30) NOT NULL
);

-- 6. OPTION 테이블 (Regular, Large, HOT, COLD 등)
CREATE TABLE OPTION (
    option_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    option_name VARCHAR(30) NOT NULL,
    extra_price INT DEFAULT 0,
    FOREIGN KEY (group_id) REFERENCES OPTIONGROUP(group_id)
);

-- 7. MENUOPTIONGROUP (메뉴와 옵션그룹 연결 - 다대다 관계 해소)
CREATE TABLE MENUOPTIONGROUP (
    menu_id BIGINT NOT NULL,
    group_id INT NOT NULL,
    PRIMARY KEY (menu_id, group_id),
    FOREIGN KEY (menu_id) REFERENCES MENU(menu_id),
    FOREIGN KEY (group_id) REFERENCES OPTIONGROUP(group_id)
);

-- 8. MEMBER 테이블
CREATE TABLE MEMBER (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    phone VARCHAR(20) UNIQUE,
    password VARCHAR(255) NOT NULL,
    age INT,
    point_balance INT DEFAULT 0,
    role VARCHAR(10) DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 9. ORDERS 테이블
CREATE TABLE ORDERS (
    order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT, -- 비회원일 경우 NULL 허용
    total_amount INT NOT NULL,
    point_used INT DEFAULT 0,
    point_earned INT DEFAULT 0,
    status VARCHAR(10) DEFAULT 'PENDING', -- PENDING, COMPLETED, CANCELLED
    ordered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    payment_type VARCHAR(20),
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id)
);

-- 10. ORDERITEM 테이블 (주문 상세)
CREATE TABLE ORDERITEM (
    order_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    category_id INT,
    quantity INT NOT NULL,
    unit_price INT NOT NULL, -- 주문 시점의 단가
    ordered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES ORDERS(order_id),
    FOREIGN KEY (menu_id) REFERENCES MENU(menu_id)
);

-- 11. ORDERITEMOPTION (주문 상품에 선택된 옵션)
CREATE TABLE ORDERITEMOPTION (
    order_item_id BIGINT NOT NULL,
    option_id INT NOT NULL,
    PRIMARY KEY (order_item_id, option_id),
    FOREIGN KEY (order_item_id) REFERENCES ORDERITEM(order_item_id)
);

-- 12. WISHLIST 테이블
CREATE TABLE WISHLIST (
    wishlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (menu_id) REFERENCES MENU(menu_id)
);

-- 13. QUICKORDER 테이블 (나만의 메뉴)
CREATE TABLE QUICKORDER (
    quick_order_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL,
    menu_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_id) REFERENCES MEMBER(member_id),
    FOREIGN KEY (menu_id) REFERENCES MENU(menu_id)
);

-- 14. QUICKORDEROPTION (나만의 메뉴에 저장된 옵션)
CREATE TABLE QUICKORDEROPTION (
    quick_order_id BIGINT NOT NULL,
    option_id INT NOT NULL,
    PRIMARY KEY (quick_order_id, option_id),
    FOREIGN KEY (quick_order_id) REFERENCES QUICKORDER(quick_order_id)
);
