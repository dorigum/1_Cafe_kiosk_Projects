USE kiosk;

-- 카테고리별 기본 옵션 그룹 설정 테이블 생성
CREATE TABLE IF NOT EXISTS `CATEGORY_OPTION_GROUP` (
  `category_id` int NOT NULL,
  `group_id` int NOT NULL,
  `display_order` int NOT NULL DEFAULT 0 COMMENT '옵션 그룹 표시 순서',
  PRIMARY KEY (`category_id`, `group_id`)
);

-- 포인트 변동 히스토리 기록 테이블 생성
CREATE TABLE IF NOT EXISTS `POINT_HISTORY` (
  `history_id` INT AUTO_INCREMENT PRIMARY KEY,
  `member_id` BIGINT NOT NULL COMMENT '회원 고유 ID',
  `amount` INT NOT NULL COMMENT '변동 금액 (+는 적립, -는 차감)',
  `reason` VARCHAR(255) NOT NULL COMMENT '변동 사유 (예: 이벤트 적립)',
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '변동 일시',
  FOREIGN KEY (`member_id`) REFERENCES `MEMBER`(`member_id`) ON DELETE CASCADE
);

-- 확인용 쿼리
SELECT * FROM `POINT_HISTORY` ORDER BY `created_at` DESC;
SELECT * FROM `ORDERS`;
SELECT * FROM `MEMBER`;