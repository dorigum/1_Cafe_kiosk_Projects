# ☕ 카페 키오스크 관리자(Admin) MVC 구조 상세 분석

본 문서는 카페 키오스크 프로젝트의 관리자 시스템에 적용된 MVC 패턴과 계층형 아키텍처, 그리고 최근 고도화된 기능들을 상세히 분석합니다.

---

## 1. 전체 아키텍처 및 계층별 역할

본 시스템은 **Layered Architecture**를 기반으로 하며, 각 계층은 엄격하게 분리된 책임을 가집니다.

### 🏛️ 계층 구조 (Layered Architecture)
- **View Layer (`view/`)**: 사용자 인터페이스(UI) 및 입출력 전담.
  - `MenuView`: 관리자 메인 메뉴 및 서브 메뉴 루프 제어.
  - `EndView`: 성공 결과 및 데이터 목록(메뉴/통계) 시각화 출력.
  - `FailView`: 에러 메시지 및 예외 상황 출력.
  - `OrderingView`: 주문 전담 인터페이스 제공.
- **Controller Layer (`controller/`)**: 요청 제어 및 흐름 관리.
  - `AdminController`: 서비스 계층 호출 및 뷰 전환 결정. (Try-Catch를 통한 전역 예외 처리 및 FailView 연동)
  - `MemberController`: 회원 관련 요청 처리.
  - `MenuController`: 사용자용 메뉴 조회 및 주문 흐름 제어.
- **Service Layer (`service/`)**: 핵심 비즈니스 로직 및 데이터 가공.
  - `AdminService / AdminServiceImpl`: 통계 계산, 유효성 검증, 데이터 변환 로직 포함.
  - `MemberService`: 회원 인증 및 포인트 관리 로직.
- **Repository Layer (`repository/`)**: 데이터베이스(DB) 접근 및 영속성 관리.
  - `CategoryRepository`, `MenuRepository`, `OrderRepository` 등: JDBC를 통한 SQL 실행 및 `DBUtil`을 이용한 커넥션 관리.
- **Model Layer (`model/`)**: 도메인 객체 및 데이터 구조 정의 (DTO/VO).
  - `Category`, `Menu`, `OptionGroup`, `Order`, `Member`, `OrderItem` 등.

---

## 2. 핵심 기능 상세 분석

### 📁 2.1. 카테고리 및 메뉴 관리 시스템 (CRUD + Option Mapping)
단순한 정보 저장을 넘어, **카테고리-메뉴-옵션** 간의 복잡한 관계를 객체 지향적으로 관리합니다.
- **카테고리 기반 옵션 자동 매핑**: `CATEGORY_OPTION_GROUP` 테이블을 통해 특정 카테고리에 속한 모든 메뉴가 공통으로 가질 옵션 그룹을 정의합니다.
- **메뉴별 상세 설정**: `Menu` 모델 내부에 `List<OptionGroup>`을 포함하여 객체 그래프 형태로 유지합니다.

### 📊 2.2. 매출 통계 및 분석 (Business Intelligence)
데이터베이스의 주문 데이터를 가공하여 고도화된 의사결정 지표를 제공합니다.
- **기간별 상세 조회**: 시작일과 종료일을 입력받아 주문 건수, 총액, 객단가(AVG)를 산출합니다.
- **시간대별 매출 분석 (Peak Time)**: `HOUR()` SQL 함수를 사용하여 피크타임을 시각화(막대 그래프)합니다.

### 👤 2.3. 회원 및 주문 관리
- **회원 시스템**: 휴대폰 번호 기반 로그인/회원가입 및 전체 목록 조회, 특정 회원 삭제 기능을 제공합니다.
- **주문 관리**: 전체 주문 목록을 조회하고, 주문 상태(`COMPLETED`, `CANCELLED`)를 관리합니다.

### 🧠 2.4. 지능형 서비스 및 데이터 무결성 고도화
- **지능형 메뉴-옵션 매핑**: 메뉴 등록 시 이름 키워드('라떼' 등)에 따라 옵션 그룹을 자동 매핑합니다.
- **주문 취소 및 포인트 복구 트랜잭션**: `JDBC Transaction`을 통해 포인트 반환 및 회수를 원자적으로 처리합니다.

---

## 3. [2026-03-15] 포인트 관리 고도화 및 보안 시스템 MVC 분석 (신규)

오늘 추가된 **포인트 히스토리 시스템**과 **관리자 보안 정책**이 각 계층에서 어떻게 동작하는지 분석합니다.

### 🎁 3.1. 포인트 변동 히스토리 (Point History System)
- **Model (`PointHistory`)**: 변동 금액, 사유, 일시를 저장하는 전용 모델을 신설하여 데이터 구조를 명확히 했습니다.
- **Repository (`MemberRepositoryImpl`)**: 
    - `POINT_HISTORY` 테이블을 자동으로 생성(`CREATE TABLE IF NOT EXISTS`)하고 관리합니다.
    - 모든 포인트 변동 발생 시 `savePointHistory()`를 통해 사유와 함께 로그를 기록합니다.
- **Service (`MemberServiceImpl`)**: 로그인한 회원이 자신의 포인트 내역을 최신순으로 조회할 수 있는 비즈니스 로직을 제공합니다.
- **View (`MenuView` & `EndView`)**: 
    - 회원이 직접 자신의 내역을 확인할 수 있는 메뉴를 추가했습니다.
    - `EndView`에서 포인트 변동 사유(예: "생일 선물 🎁")를 시각적으로 가독성 있게 출력합니다.

### 🛡️ 3.2. 관리자 계정 보호 및 1인 정책 (Security Policy)
- **Service (`AdminServiceImpl`)**: 시스템의 '헌법'과 같은 비즈니스 규칙을 강제합니다.
    - **1인 관리자 제한**: `ADMIN` 등급 변경 시 현재 관리자 존재 여부를 체크하여 중복 임명을 방지합니다.
    - **삭제 보호**: 삭제 대상이 `ADMIN`이면 예외(`BusinessRuleException`)를 발생시켜 직접 삭제를 원천 차단합니다.
- **Controller (`AdminController`)**: 서비스 계층의 보호 로직에서 발생하는 예외를 포착하여 사용자에게 "관리자 보호 메시지"를 일관되게 전달합니다.
- **View (`MenuView`)**: 
    - 포인트 수정 시 관리자 ID를 입력하면 즉시 등급을 체크하여 금액 입력 단계로 넘어가지 않도록 흐름을 개선했습니다 (UX 고도화).

---

## 4. 주요 기술적 특징 (Technical Highlights)
- **객체-관계 매핑 (ORM 유사 구현)**: JDBC 환경에서 N:M 관계를 효율적으로 처리하기 위한 Collection Fetching 기법 적용.
- **데이터 정합성 유지**: Atomic Transaction 처리를 통한 포인트 복구 시스템 구축.

---

## 5. 환경 설정 및 협업 가이드
- **리소스 최적화**: `dbInfo.properties` 대소문자 정규화 및 클래스패스 로딩 안정화.
- **인코딩 표준**: 전 계층 **UTF-8** 적용으로 한글 데이터 무결성 보장.
