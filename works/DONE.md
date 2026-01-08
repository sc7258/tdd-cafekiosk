# 완료된 작업 목록 (Done)
이 문서는 완료된 개발 및 리팩토링 작업 내역을 기록합니다.

### Phase 3: 주문 기능 구현 (TDD) - 주문 관리 (2026-01-08)
- [x] API 명세 정의: `openapi/cafe-kiosk-api-v1.yaml`에 주문 상태 변경 API 명세 추가
- [x] API 코드 생성: 빌드하여 `OrderApi`, 관련 모델 등 생성
- [x] API 계층 구현: `OrderApiDelegateImpl` 클래스에 `updateOrderStatus` 구현
- [x] 주문 상태 변경 API 컨트롤러 테스트 코드 작성
- [x] 도메인 계층 구현: `OrderService`에 주문 상태 변경 비즈니스 로직 구현
- [x] 주문 상태 변경 서비스 테스트 코드 작성
- [x] 주문 관리 테스트 시나리오:
    - 주문 생성 시 주문 시간과 주문 번호가 올바르게 기록된다.
    - 주문 상태를 'INIT'에서 'COMPLETED'로 변경할 수 있다.
    - 존재하지 않는 주문의 상태를 변경하려고 하면 예외가 발생한다.
    - 유효하지 않은 상태로 변경하려고 하면 예외가 발생한다.

### Phase 4: OpenAPI 원본 명세 제공 (2026-01-08)
- [x] **파일 이동**: `openapi/cafe-kiosk-api-v1.yaml` 파일을 `src/main/resources/static/openapi/`로 이동 (Gradle 태스크 `copyOpenApiSpec`으로 자동화)
- [x] **빌드 설정 업데이트**: `build.gradle`의 `openApiGenerate` 태스크 `inputSpec` 경로 업데이트 및 `copyOpenApiSpec` 태스크 의존성 추가
