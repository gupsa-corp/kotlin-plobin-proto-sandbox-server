# SandboxTemplateVersion 테스트 스위트

이 프로젝트의 비즈니스 로직을 위한 포괄적인 테스트 코드입니다.

## 테스트 구조

### 단위 테스트 (Unit Tests)
각 컨트롤러 클래스에 대한 독립적인 테스트:

- **SandboxTemplateVersionCreateControllerTest**: 버전 생성 로직 테스트
  - ✅ 정상적인 버전 생성
  - ✅ null 설명 처리
  - ✅ 자동 타임스탬프 생성

- **SandboxTemplateVersionUpdateControllerTest**: 버전 업데이트 로직 테스트
  - ✅ 기존 버전 업데이트
  - ✅ 존재하지 않는 버전 처리 (null 반환)
  - ✅ null 설명 처리
  - ✅ 원본 생성 시간 및 템플릿 ID 보존

- **SandboxTemplateVersionDeleteControllerTest**: 버전 삭제 로직 테스트
  - ✅ 기존 버전 삭제
  - ✅ 존재하지 않는 버전 처리 (null 반환)
  - ✅ 삭제 순서 보장 (조회 후 삭제)

- **SandboxTemplateVersionGetControllerTest**: 단일 버전 조회 테스트
  - ✅ 기존 버전 조회
  - ✅ 존재하지 않는 버전 처리 (null 반환)
  - ✅ null 설명이 있는 엔티티 처리
  - ✅ 모든 필드 매핑 정확성

- **SandboxTemplateVersionListControllerTest**: 전체 버전 목록 조회 테스트
  - ✅ 여러 버전이 있는 경우 전체 반환
  - ✅ 빈 리스트인 경우 빈 배열 반환
  - ✅ 단일 엔티티 처리
  - ✅ 리포지터리 순서 보존

### 통합 테스트 (Integration Tests)

- **SandboxTemplateVersionRepositoryTest**: JPA 리포지터리 계층 테스트
  - ✅ findBySandboxTemplateId: 특정 템플릿의 버전들 조회
  - ✅ findByVersionNameContaining: 버전명 검색
  - ✅ findBySandboxTemplateIdOrderByCreatedAtDesc: 생성일 기준 내림차순 정렬
  - ✅ findBySandboxTemplateIdAndVersionNumber: 특정 템플릿의 특정 버전 조회
  - ✅ null 설명이 있는 엔티티 처리
  - ✅ 새 엔티티 저장과 ID 자동 생성

- **SandboxTemplateVersionWorkflowIntegrationTest**: 전체 CRUD 워크플로우 테스트
  - ✅ 완전한 CRUD 워크플로우 (생성→조회→업데이트→삭제)
  - ✅ 여러 템플릿에 대한 여러 버전 처리
  - ✅ 엣지 케이스 처리 (존재하지 않는 엔티티들)
  - ✅ 데이터 무결성 제약 조건 유지

- **SandboxTemplateVersionBusinessRulesTest**: 비즈니스 규칙 검증 테스트
  - ✅ Sandbox Template ID는 양수여야 함
  - ✅ Version Name은 공백일 수 없음
  - ✅ Version Name 길이 제한 (100자)
  - ✅ Version Number는 공백일 수 없음
  - ✅ Version Number 길이 제한 (50자)
  - ✅ Description은 선택사항 (null 허용)
  - ✅ 타임스탬프 자동 관리
  - ✅ 템플릿 내 버전 고유성
  - ✅ 다른 템플릿 간 동일 버전 번호 허용

## 테스트 실행

### 전체 테스트
```bash
./gradlew test
```

### 특정 테스트 클래스
```bash
./gradlew test --tests "SandboxTemplateVersionCreateControllerTest"
```

### 패턴 매칭
```bash
./gradlew test --tests "*CreateController*"
./gradlew test --tests "*Integration*"
```

## 커버리지

### 비즈니스 로직 커버리지
- **컨트롤러 계층**: 100% - 모든 CRUD 오퍼레이션
- **서비스 계층**: N/A - 컨트롤러에서 직접 리포지터리 호출
- **리포지터리 계층**: 100% - 모든 쿼리 메서드
- **엔티티 계층**: 100% - 모든 필드 매핑

### 시나리오 커버리지
- **정상 플로우**: ✅ 모든 CRUD 오퍼레이션
- **예외 상황**: ✅ 존재하지 않는 엔티티, null 값 처리
- **엣지 케이스**: ✅ 빈 리스트, 경계값, 제약 조건
- **데이터 무결성**: ✅ 외래 키, 유효성 검증, 타임스탬프

## 테스트 설정

### 의존성
```kotlin
testImplementation("org.springframework.boot:spring-boot-starter-test")
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("com.ninja-squad:springmockk:4.0.2")
testImplementation("com.h2database:h2")
```

### 테스트 데이터베이스
- **H2 인메모리 데이터베이스**: 각 테스트마다 초기화
- **테스트 프로필**: `@ActiveProfiles("test")`
- **DDL 자동 생성**: `hibernate.ddl-auto=create-drop`

## 베스트 프랙티스 적용

### 테스트 격리
- 각 테스트는 독립적으로 실행
- @BeforeEach에서 테스트 데이터 초기화
- @DataJpaTest 트랜잭션 자동 롤백

### Mock 사용
- 외부 의존성은 MockK로 모킹
- 리포지터리 계층만 실제 데이터베이스 사용

### 검증 방식
- **상태 검증**: 반환값과 데이터베이스 상태 확인
- **행위 검증**: 메서드 호출 횟수와 파라미터 확인

### 테스트 명명 규칙
- **한국어 메서드명**: `테스트할_내용을_테스트한다()` 패턴 사용
- **Given-When-Then** 구조의 테스트 본문
- 비즈니스 의도가 명확하게 드러나는 네이밍
- 백틱(`) 사용으로 가독성 향상

**예시**:
```kotlin
@Test
fun `새로운_버전_생성을_테스트한다`() {
    // Given - 테스트 데이터 준비
    // When - 실행
    // Then - 검증
}
```

이 테스트 스위트는 SandboxTemplateVersion의 모든 비즈니스 로직을 포괄적으로 검증하며, 향후 코드 변경 시 회귀 버그를 방지하는 안전망 역할을 합니다.