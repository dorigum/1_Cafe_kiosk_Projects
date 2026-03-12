# ☕ 카페 키오스크 프로젝트 명세서 (Cafe Kiosk Project)

## 1. 프로젝트 개요
- **목표**: MVC 패턴을 적용한 콘솔 기반 카페 키오스크 시스템 구축 (Layered Architecture)
- **주요 기능**: 회원/비회원 주문 서비스 및 관리자 모드 (상품/카테고리/회원/매출 관리)
- **개발 환경**: Java 18 (OpenJDK), **MySQL 8.0**, JDBC (mysql-connector-j-9.2.0)

## 2. 주요 기능 요구사항 (진행 현황)

### 👤 사용자(회원/비회원) 기능
- [x] **회원 로그인**: 휴대폰 번호/비밀번호 인증 (`MemberRepository` 연동)
- [x] **주문 내역 조회**: JOIN 쿼리를 통한 상세 내역 확인
- [ ] 상품 선택 및 장바구니 담기 (구현 예정)
- [ ] 결제 처리 및 포인트 적립 (구현 예정)

### 🛠️ 관리자(Admin) 통합 관리 시스템
- [x] **카테고리 관리 (CRUD)**: 카테고리 추가, 목록 조회, 삭제
- [x] **상품(메뉴) 관리 (CRUD)**: 카테고리별 상품 등록, 조회, 삭제
- [x] **회원 관리**: 가입된 전체 회원 목록 조회 및 특정 회원 삭제
- [x] **매출 통계 및 분석**:
    - 누적 총 매출액 집계
    - **카테고리별 매출 분석** (Coffee, Tea 등)
    - **인기 메뉴 Top 3** 선정 (판매량 기준)
    - **일별 매출 추이 시각화** (최근 7일 막대 그래프)
- [x] **주문 관리**: 전체 주문 목록 확인 및 **결제 취소(CANCELLED)** 처리

## 3. 시스템 아키텍처 (Layered Architecture)
- **Model**: `Product`, `Member`, `OrderHistory` (DB 테이블 대응 객체)
- **View**: `MainView` (최상위 진입점 및 메인 메뉴)
- **Controller**: `AdminController` (관리자 인터페이스 및 흐름 제어)
- **Service**: `AdminService`, `MemberService` (비즈니스 로직 및 데이터 가공)
- **Repository**: `ProductRepository`, `MemberRepository`, `CategoryRepository`, `OrderRepository` (JDBC 기반 SQL 처리)
- **Configuration**: `dbinfo.properties` (외부 설정 파일을 통한 DB 정보 관리)

## 4. 데이터 설계 (MySQL 테이블 구조)
| 구분 | 테이블명 | 설명 |
| :--- | :--- | :--- |
| **Category** | CATEGORY | 상품 분류 (Coffee, Tea, Dessert 등) |
| **Product** | MENU | 상품명, 가격, 설명, 품절 여부 |
| **Option** | OPTION_GROUP, OPTION | 옵션 그룹(사이즈, 온도 등) 및 세부 옵션 |
| **Mapping** | MENU_OPTION_GROUP | 메뉴별 적용 가능한 옵션 그룹 매핑 |
| **Member** | MEMBER | 회원 정보, 휴대폰 번호(Unique), 포인트, 역할 |
| **Order** | ORDERS | 주문 총액, 사용/적립 포인트, 주문 상태, 주문 일시 |
| **OrderItem** | ORDER_ITEM | 주문 상세(수량, 단가), 메뉴/카테고리명 스냅샷 포함 |
| **OrderItemOption**| ORDER_ITEM_OPTION | 주문 시점에 선택된 세부 옵션 기록 |
| **Wishlist** | WISHLIST | 회원의 찜 목록 |

## 5. 진행 현황 및 히스토리
- **[2026-03-11]** **프로젝트 환경 고도화 및 이클립스 최적화**
  - `Main.java` → `view.MainView.java`로 변경 및 패키지 구조화
  - 콘솔 한글 깨짐 방지를 위한 **UTF-8 강제 입출력 로직** 적용
  - 이클립스 프로젝트 설정 파일(`.project`, `.classpath`) 생성 및 `lib` 폴더 라이브러리 추가
- **[2026-03-11]** **데이터베이스 보안 및 설정 개선**
  - `dbinfo.properties` 도입으로 DB 접속 정보 외부화 (보안 강화)
  - `DBUtil` 클래스에서 Properties 파일을 통한 동적 접속 기능 구현
- **[2026-03-11]** **관리자 통합 관리 및 매출 통계 시스템 완성**
  - 일별 매출 그래프 및 주문 취소 기능 구현 완료

- **[2026-03-12]** **AdminController 리팩토링 및 MVC 패턴 고도화**
  - **Controller 역할 정립**: `AdminController`에서 입출력 로직을 제거하고 서비스 호출 및 뷰 전환 기능만 담당하도록 리팩토링 (Static 메소드 기반)
  - **View 레이어 세분화**:
    - `EndView`: 성공 메시지 및 데이터 목록(메뉴/카테고리/회원/주문) 출력 전담
    - `FailView`: 예외 상황 발생 시 에러 메시지 출력 전담
    - `AdminMenuView`: 콘솔 메뉴 반복문 및 `Scanner` 입력 로직을 분리하여 UI 제어 담당
  - **Service 인터페이스 개선**: `AdminService.cancelOrder` 반환 타입을 `boolean`으로 변경하여 컨트롤러에서 결과에 따른 분기 처리 구현

- **[2026-03-12]** **DB 제약 조건 강화 및 UI/UX 개선**
  - **데이터 무결성 강화**:
    - `CATEGORY` 테이블의 `category_name`에 `UNIQUE` 제약 조건 추가 (중복 방지)
    - `MENU` 테이블의 `menu_name`에 `UNIQUE` 제약 조건 추가
  - **주문 관리 UI 개선**:
    - 주문 목록 출력 시 상태별 마크 표시 (`[V]` 완료, `[X]` 취소)
    - '일시'를 '주문일시'로 명칭 변경 및 주문자(ID) 정보 추가
    - 주문 취소 로직 수정: `COMPLETED` 상태뿐만 아니라 이미 취소되지 않은 모든 주문 취소 가능하도록 변경
  - **메뉴 관리 UI 개선**:
    - 메뉴 목록 출력 시 카테고리 ID 대신 **카테고리 이름**이 출력되도록 `JOIN` 쿼리 적용
    - 메뉴 이름 출력 여백 조정 및 설명(`description`) 정보 추가
  - **메뉴 등록 로직 고도화**:
    - 존재하지 않는 카테고리 ID 입력 시 명확한 에러 메시지 출력 및 **성공할 때까지 재입력** 받는 루프 로직 구현
  - **DB 스크립트 안정화**: `DDL.sql`에 테이블 자동 삭제/생성 및 데이터베이스 초기화 로직 추가
  - **매출 통계 시스템 고도화**:
    - **시각화 복구**: `■` 문자를 활용한 막대 그래프 출력 로직 복구 및 디자인 개선
    - **기간별 필터 도입**: 일별, 주차별, 월별, 연도별 매출 추이를 선택하여 조회할 수 있는 필터링 기능 구현
    - **UI 개선**: 통계 분석 전용 서브 메뉴 추가 및 리포트 가독성 향상
  - **매출 통계 메뉴 체계화**:
    - **메뉴 재구성**: '매출 통계 및 그래프 조회'로 명칭 변경 및 3대 분석 카테고리(날짜별, 카테고리별, 메뉴별) 도입
    - **분석 세분화**:
      - 날짜별: 일/주/월/년 단위 매출 추이 그래프
      - 카테고리별: 카테고리 점유율 및 매출액 분석 (백분율 표시 추가)
      - 메뉴별: 판매 순위(TOP 10) 및 판매량 기반 분석
    - **포맷팅 강화**: 주차별 데이터를 'YYYY-MM월 W주' 형식으로 변환하여 가독성 극대화
  - **보안 및 인증 시스템 강화**:
    - **관리자 전용 로그인**: 관리자 모드 진입 전 별도 인증 절차 도입 및 `ADMIN` 권한 검증 로직 추가
    - **전화번호 정규화**: 로그인 시 하이픈(`-`) 유무와 관계없이 숫자만으로 동일 계정 인식 및 포맷 자동 변환 기능 구현
    - **보안성 향상**: MySQL `BINARY` 키워드를 활용하여 비밀번호 대소문자를 엄격히 구분하도록 인증 쿼리 개선
  - **사용자 경험(UX) 개선**:
    - **맞춤형 환영 메시지**: 관리자 로그인 시 포인트 정보를 제외한 전용 환영 문구 출력 로직 적용
    - **디자인 세밀화**: 통계 리포트의 구분선 및 아이콘 배치 조정으로 가독성 향상

- **[2026-03-11]** **주요 작업 내역**
   - **최신 DDL 반영**: `ORDER_ITEM` 스냅샷 구조 도입 및 명명 규칙 통일
   - **문서화**: `Project_Specification.md`에 변경 내역 및 협업 가이드 작성
   - **공유 완료**: GitHub `feature/cafe-admin` 브랜치 푸시 및 PR 생성 완료

## 6. 개발 및 협업 가이드 (실행 전 확인)
1.  **MySQL 설정**: `resources/schema.sql` -> `resources/data.sql` 순서로 실행
2.  **DB 접속 정보**: `resources/dbinfo.properties` 파일에서 본인의 MySQL 계정/비번 수정
3.  **라이브러리**: `lib/mysql-connector-j-9.2.0.jar`가 Build Path에 포함되어 있는지 확인
4.  **인코딩**: 이클립스 실행 설정(Run Configurations)에서 **Encoding을 UTF-8**로 지정 필수

## 7. 협업을 위한 변경사항
- **최신 DDL 반영 및 테이블 구조 최적화**:
  - `ORDERITEM` → `ORDER_ITEM`, `OPTIONGROUP` → `OPTION_GROUP` 등 테이블명 명명 규칙 통일 (Snake Case)
  - `ORDER_ITEM` 테이블에 `menu_name_snapshot`, `category_name_snapshot` 컬럼 추가하여 데이터 정합성 강화 (메뉴명 변경 시에도 과거 주문 내역 보존)
  - `ORDERS` 테이블의 `ordered_at` → `order_date` 컬럼명 변경
- **소스 코드 동기화**:
  - `OrderRepository`, `OrderItem`, `Order` 클래스의 필드 및 SQL 쿼리를 변경된 DDL에 맞춰 전수 업데이트
  - 인덱스(Index) 설정을 통해 대용량 주문 조회 및 매출 통계 쿼리 성능 최적화
- **DB 스크립트 현행화**:
  - 루트의 `ddl.sql` 내용을 `resources/schema.sql`에 반영하여 실행 환경 일치화
  - 변경된 스키마 구조에 맞춰 샘플 데이터(`data.sql`) 재구성
