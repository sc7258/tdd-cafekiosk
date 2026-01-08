# TODO

## 작업할 일

### Phase 3: 주문 기능 구현 (TDD) - 주문 생성

- [x] **API 명세 정의**: `openapi/cafe-kiosk-api-v1.yaml`에 주문 생성 API 명세 추가 (2026-01-08)
    - `POST /api/v1/orders`
- [x] **API 코드 생성**: 빌드하여 `OrderApi`, 관련 모델 등 생성 (2026-01-08)
- [x] **API 계층 구현**:
    - [x] `OrderApiDelegateImpl` 클래스 생성 및 `OrderApiDelegate` 인터페이스 구현 (2026-01-08)
    - [x] 주문 생성 API 컨트롤러 테스트 코드 작성 (2026-01-08)
- [x] **도메인 계층 구현**:
    - [x] `Order`, `OrderProduct` 엔티티 및 `OrderRepository` 인터페이스 정의 (2026-01-08)
    - [x] 주문 상태(`OrderStatus`) Enum 클래스 정의 (2026-01-08)
    - [x] 주문 생성 서비스 테스트 코드 작성 (2026-01-08)
        - 여러 개의 상품으로 주문을 생성하는 경우
        - 주문 생성 시 주문 상태가 'INIT'이 되는 경우
        - 주문 총액이 올바르게 계산되는 경우
    - [x] `OrderService` 클래스 생성 및 비즈니스 로직 구현 (2026-01-08)
- [x] **Liquibase 설정**: `orders`, `order_product` 테이블 생성을 위한 `changelog` 파일 작성 (2026-01-08)

### Phase 3: 주문 기능 구현 (TDD) - 주문 상태 관리 (2026-01-08)
- [x] **API 명세 정의**: `openapi/cafe-kiosk-api-v1.yaml`에 주문 상태 변경 API 명세 추가
    - `PATCH /api/v1/orders/{orderId}/status`
- [x] **API 코드 생성**: 빌드하여 `OrderApi`, 관련 모델 등 생성
- [x] **API 계층 구현**:
    - [x] `OrderApiDelegateImpl` 클래스에 `updateOrderStatus` 구현
    - [x] 주문 상태 변경 API 컨트롤러 테스트 코드 작성
- [x] **도메인 계층 구현**:
    - [x] `OrderService`에 주문 상태 변경 비즈니스 로직 구현
    - [x] 주문 상태 변경 서비스 테스트 코드 작성

- [x] **주문 상태 변경 테스트 시나리오**:
    - [x] 주문 상태를 'INIT'에서 'COMPLETED'로 변경할 수 있다.
    - [x] 존재하지 않는 주문의 상태를 변경하려고 하면 예외가 발생한다.
    - [x] 유효하지 않은 상태로 변경하려고 하면 예외가 발생한다.

### Phase 3: 주문 기능 구현 (TDD) - 주문 목록 관리 및 금액 계산
- [ ] **주문 목록 관리 기능 구현**:
    - [ ] 주문 목록에 상품을 추가할 수 있다.
    - [ ] 주문 목록에서 특정 상품을 삭제할 수 있다.
    - [ ] 주문 목록의 모든 상품을 삭제할 수 있다.
    - [ ] 주문 목록에 담긴 상품의 개수를 변경할 수 있다.
    - [ ] 상품 개수를 0개 이하로 변경하려고 하면 예외가 발생한다.
- [ ] **금액 계산 기능 구현**:
    - [ ] 주문 목록에 있는 모든 상품의 총 금액을 올바르게 계산한다.

### Phase 4: OpenAPI 원본 명세 제공 (2026-01-08)
- [x] **파일 이동**: `openapi/cafe-kiosk-api-v1.yaml` 파일을 `src/main/resources/static/openapi/`로 이동 (Gradle 태스크 `copyOpenApiSpec`으로 자동화)
- [x] **빌드 설정 업데이트**: `build.gradle`의 `openApiGenerate` 태스크 `inputSpec` 경로 업데이트 및 `copyOpenApiSpec` 태스크 의존성 추가

## 완료된 일

### Phase 2: 메뉴 기능 구현 (TDD) (2026-01-08)
- [x] API 명세 정의, 코드 생성, API 계층 및 도메인 계층 구현, Liquibase 설정 완료

### Phase 1: 프로젝트 초기 설정 및 API First 환경 구축 (2026-01-08)
- [x] jOOQ, Modulith, openapi-generator 등 초기 환경 설정 완료
