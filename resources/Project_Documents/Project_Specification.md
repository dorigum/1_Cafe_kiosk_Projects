# ☕ 카페 키오스크 프로젝트 명세서 (Cafe Kiosk Project)

## 1. 프로젝트 개요
- **목표**: MVC 패턴을 적용한 콘솔 기반 카페 키오스크 시스템 구축 (Layered Architecture)
- **주요 기능**: 회원/비회원 주문 서비스 및 관리자 모드 (상품/카테고리/회원/매출 관리)
- **개발 환경**: Java 18 (OpenJDK), **MySQL 8.0**, JDBC (mysql-connector-j-9.6.0)

## 2. 주요 기능 요구사항 (진행 현황)

### 👤 사용자(회원/비회원) 기능
- [x] **회원 로그인**: 휴대폰 번호/비밀번호 인증 (`MemberRepository` 연동)
- [x] **주문 내역 조회**: JOIN 쿼리를 통한 상세 내역 확인
- [x] **포인트 내역 확인**: 본인의 포인트 적립 및 차감 히스토리(사유 포함) 조회 기능 (신규)
- [ ] 상품 선택 및 장바구니 담기 (구현 예정)
- [ ] 결제 처리 및 포인트 적립 (구현 예정)

### 🛠️ 관리자(Admin) 통합 관리 시스템
- [x] **카테고리 관리 (CRUD)**: 카테고리 추가, 목록 조회, 삭제
- [x] **메뉴 및 옵션 관리 (CRUD)**: 
    - **메뉴 정보 관리**: 카테고리별 상품 등록, 조회, 삭제
    - **메뉴 옵션 관리**: 옵션 그룹(온도, 사이즈 등) 및 세부 옵션(ICE, Regular 등) 관리 기능 추가
    - **카테고리별 옵션 설정**: 각 카테고리에 기본 적용될 옵션 그룹 매핑 설정 기능 추가
- [x] **회원 관리 및 보안**: 
    - 가입된 전체 회원 목록 조회 및 특정 회원 삭제
    - **포인트 수정 고도화**: 특정 회원에게 포인트 지급/차감 시 **수정 사유**를 필수 입력하도록 개선 (신규)
    - **등급(Role) 관리**: 일반 회원(USER)과 관리자(ADMIN) 등급 변경 기능 추가 (신규)
    - **1인 관리자 체제**: 시스템 안정성을 위해 관리자는 **단 1명만 존재**할 수 있도록 제한 로직 구현 (신규)
    - **관리자 보호 로직**: 관리자 계정은 포인트 수정 및 직접 삭제가 불가능하도록 안전 장치 마련 (신규)
- [x] **매출 통계 및 분석**:
    - 누적 총 매출액 집계
    - **카테고리별 매출 분석** (Coffee, Tea 등)
    - **인기 메뉴 Top 3** 선정 (판매량 기준)
    - **일별 매출 추이 시각화** (최근 7일 막대 그래프)
    - **기간별 상세 조회**: 날짜 범위 지정을 통한 매출액 및 **객단가(AVG)** 분석 (신규)
    - **시간대별 매출 분석**: 하루 중 주문 집중 시간대(피크타임) 파악 (신규)
    - **우수 회원 기여도 분석**: 누적 결제액 기준 상위 **VVIP 회원** 추출 (신규)
- [x] **주문 관리**: 전체 주문 목록 확인 및 **결제 취소(CANCELLED)** 처리

## 3. 시스템 아키텍처 (Layered Architecture)
- **Model**: `Menu`, `Member`, `Order`, `MenuOption`, `OptionGroup`, `PointHistory` (신규)
- **View**: `StartView` (진입점), `MenuView` (메인 루프), `EndView` (출력 전담), `FailView` (에러 전담), `OrderingView` (주문 전담)
- **Controller**: `AdminController`, `MenuController`, `MemberController`
- **Service**: `AdminService`, `MemberService`, `MenuService`
- **Repository**: `MenuRepository`, `MemberRepository`, `CategoryRepository`, `OrderRepository`, `MenuOptionRepository`, `OptionGroupRepository`
- **Configuration**: `dbInfo.properties` (파일명 대소문자 수정 및 소스 폴더 경로 최적화)

## 4. 데이터 설계 (MySQL 테이블 구조)
| 구분 | 테이블명 | 설명 |
| :--- | :--- | :--- |
| **Category** | CATEGORY | 상품 분류 (Coffee, Tea, Dessert 등) |
| **Menu** | MENU | 상품명, 가격, 설명, 품절 여부 |
| **MenuOption** | MENU_OPTION | 세부 메뉴 옵션 (HOT, ICE, Regular, Large 등) |
| **OptionGroup** | OPTION_GROUP | 옵션 그룹 (온도, 사이즈, 카페인유무 등) |
| **CategoryOption** | CATEGORY_OPTION_GROUP | 카테고리별 기본 적용 옵션 그룹 매핑 |
| **Mapping** | MENU_OPTION_GROUP | 메뉴별 적용 가능한 옵션 그룹 매핑 |
| **Member** | MEMBER | 회원 정보, 휴대폰 번호(Unique), 포인트, 역할(ADMIN/USER) |
| **PointHistory** | POINT_HISTORY | 포인트 변동 내역 및 사유 기록 테이블 (신규) |
| **Order** | ORDERS | 주문 총액, 사용/적립 포인트, 주문 상태, 주문 일시 |
| **OrderItem** | ORDER_ITEM | 주문 상세(수량, 단가), 메뉴/카테고리명 스냅샷 포함 |
| **OrderItemOption**| ORDER_ITEM_OPTION | 주문 시점에 선택된 세부 옵션 기록 |
| **Wishlist** | WISHLIST | 회원의 찜 목록 |

## 5. 진행 현황 및 히스토리

### [2026-03-15] 포인트 관리 고도화 및 관리자 보안 시스템 구축
- **포인트 투명성 강화 (History System)**:
    - `POINT_HISTORY` 테이블 및 모델을 신설하여 모든 포인트 변동(지급/차감/사용/적립) 내역을 사유와 함께 기록.
    - 회원이 로그인 후 본인의 포인트 변동 사유(예: "회원 생일 이벤트 🎁")를 직접 확인할 수 있는 메뉴 추가.
    - 관리자가 포인트 수정 시 **'수정 사유'를 필수 입력**하도록 UI 및 비즈니스 로직 확장.
- **관리자 계정 보호 및 1인 관리자 정책**:
    - **1인 관리자 제한**: 시스템 내 `ADMIN` 역할을 가진 회원은 단 1명만 존재하도록 제한하여 권한 오남용 방지.
    - **삭제 보호**: 관리자 계정은 직접 삭제가 불가능하며, 반드시 `USER` 등급으로 변경한 후에만 삭제 가능하도록 안전 장치 구현.
    - **수정 보호**: 관리자 등급의 회원은 포인트 수정 대상에서 제외하여 데이터 무결성 확보.
- **회원 등급 관리(Role Management) 도입**:
    - 관리자가 회원의 등급을 `ADMIN` <-> `USER`로 변경할 수 있는 기능 추가.
    - 등급 변경 시 1인 관리자 정책에 따라 기존 관리자 해임 후 신규 관리자 임명 방식 유도.
- **환경 설정 최적화 및 프로젝트 리네이밍**:
    - 프로젝트 폴더명을 `1_Cafe_kiosk_Projects`로 변경하고 이에 따른 GitHub 원격 저장소 재설정 완료.
    - `dbInfo.properties` 대소문자 정규화 및 이클립스 `Build Path` 최적화(`.classpath` 수동 구성)를 통해 리소스 로딩 안정화.

### [2026-03-13] 주문 취소 포인트 복구 시스템 및 관리자 UI/UX 고도화
- **지능형 메뉴 등록 로직 도입**:
    - 메뉴 이름에 특정 키워드(**'라떼', '프라푸치노'**)가 포함된 경우 '휘핑유무' 옵션 그룹을 자동으로 매핑하는 지능형 서비스 로직 구현.
- **주문 취소 및 포인트 정합성 보장**:
    - `CANCELLED` 처리 시 사용자가 주문 시 사용했던 포인트는 **반환**하고, 주문으로 인해 적립되었던 포인트는 **자동 회수**하는 비즈니스 로직 구현.
- **관리자 가독성 및 조작 편의성 개선**:
    - **주문 관리**: 주문 목록 조회 시 회원 ID 대신 **휴대폰 번호**를 출력하도록 개선.
    - **메뉴 관리**: 메뉴 목록 출력 시 실제 **데이터베이스 ID(`[메뉴 ID: X]`)** 노출로 식별성 강화.

... (중략: 이전 히스토리 유지) ...

## 6. 개발 및 협업 가이드 (실행 전 확인)
1.  **MySQL 설정**: `resources/DDL.sql` -> `resources/DML.sql` -> `resources/admin.sql` 순서로 실행
2.  **DB 접속 정보**: `resources/dbInfo.properties` 파일에서 본인의 MySQL 계정/비번 수정
3.  **라이브러리**: `lib/mysql-connector-j-9.6.0.jar`가 Build Path에 포함되어 있는지 확인
4.  **인코딩**: 이클립스 실행 설정(Run Configurations)에서 **Encoding을 UTF-8**로 지정 필수
