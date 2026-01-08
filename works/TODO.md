# TODO

## 작업할 일

(남은 작업 없음)

## 완료된 일

### Phase 3: 주문 기능 구현 (TDD) (2026-01-08)
- **주문 생성**:
    - [x] API 명세 정의, 코드 생성, API 계층 및 도메인 계층 구현, Liquibase 설정 완료
- **주문 상태 관리**:
    - [x] API 명세 정의, 코드 생성, API 계층 및 도메인 계층 구현 완료
- **주문 목록 관리 및 금액 계산**:
    - [x] 주문 목록 관리 기능 (추가, 삭제, 전체 삭제, 수량 변경) 구현 완료
    - [x] 금액 계산 기능 구현 완료

### Phase 4: OpenAPI 원본 명세 제공 (2026-01-08)
- [x] **파일 이동**: `openapi/cafe-kiosk-api-v1.yaml` 파일을 `src/main/resources/static/openapi/`로 이동 (Gradle 태스크 `copyOpenApiSpec`으로 자동화)
- [x] **빌드 설정 업데이트**: `build.gradle`의 `openApiGenerate` 태스크 `inputSpec` 경로 업데이트 및 `copyOpenApiSpec` 태스크 의존성 추가

### Phase 2: 메뉴 기능 구현 (TDD) (2026-01-08)
- [x] API 명세 정의, 코드 생성, API 계층 및 도메인 계층 구현, Liquibase 설정 완료

### Phase 1: 프로젝트 초기 설정 및 API First 환경 구축 (2026-01-08)
- [x] jOOQ, Modulith, openapi-generator 등 초기 환경 설정 완료
