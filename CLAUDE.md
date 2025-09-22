# Plobin Proto V3 개발 가이드

계정 : admin@example.com
비번 : password

## 최우선 준수 규칙

## 백엔드 API 개발 규칙

### API 구조 및 네이밍
**절대 원칙**: 모든 백엔드 파일은 **폴더명/기능명/파일타입.php** 구조 사용

**필수 파일 구조**
- 컨트롤러: `app/Http/Controllers/{도메인}/{액션}/Controller.php`
- 요청 검증: `app/Http/Controllers/{도메인}/{액션}/Request.php`
- 응답 처리: `app/Http/Controllers/{도메인}/{액션}/Response.php`

**올바른 예시**
```
app/Http/Controllers/DocumentsSummary/Analyze/Controller.php
app/Http/Controllers/DocumentsSummary/Analyze/Request.php
app/Http/Controllers/DocumentsSummary/Analyze/Response.php
```

**금지사항**
- `SimplifiedController.php`
- `UserCreateController.php`
- `CreateUserRequest.php`

### 핵심 규칙
- 1개 파일 = 1개 메서드만
- 클래스명은 `Controller`, `Request`, `Response`로 통일
- Request는 POST/PUT 액션에만 생성
- **Response는 모든 API 액션에 생성 필수** - API 응답 구조화 및 일관성 확보

### Exception 처리 규칙
**필수 파일 구조**
- Exception: `app/Exceptions/{도메인}/{예외명}/Exception.php`

**핵심 규칙**
- 1개 파일 = 1개 Exception 클래스만
- 클래스명은 반드시 `Exception`으로 끝남
- Laravel의 Exception 클래스 상속 필수
- 도메인별로 폴더 분리
- 의미있는 에러 메시지와 함께 생성자 제공
- Exception 파일은 `app/Exceptions/{도메인}/{예외명}/Exception.php` 구조로 생성
- 환경변수 누락 등 설정 관련 예외는 Config 도메인 사용

**올바른 예시**
```
app/Exceptions/Config/EnvironmentVariableNotSet/Exception.php
app/Exceptions/LocalAgent/ToolNotFound/Exception.php
app/Exceptions/LlmChat/ApiResponse/Exception.php
app/Exceptions/Jobs/Processing/Exception.php
```

**금지사항**
- 하나의 파일에 여러 Exception 클래스
- Exception이 아닌 클래스명 사용
- 일반적인 \Exception, \InvalidArgumentException 직접 사용

### API 응답 표준
**성공 응답**
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다",
  "data": {
    "request_id": 12345
  }
}
```

**실패 응답**
```json
{
  "success": false,
  "message": "오류 메시지",
  "data": null
}
```

### 라우팅 설정
**API 라우트 등록**: `routes/api.php`에 등록하고 `bootstrap/app.php`에서 API 라우트 활성화 필수

```php
// bootstrap/app.php
->withRouting(
    web: __DIR__.'/../routes/web.php',
    api: __DIR__.'/../routes/api.php',  // 이 줄 필수!
    commands: __DIR__.'/../routes/console.php',
    health: '/up',
)
```

## 테스트 개발 규칙

### 테스트 파일 구조
**Feature 테스트**: `tests/Feature/{도메인}{액션}Test.php`
- 예시: `tests/Feature/DocumentsSummaryAnalyzeTest.php`

### 테스트 메서드명 규칙
**한글 메서드명 사용**: `test_{기능}을_한다()` 형식으로 작성

**올바른 예시**
```php
public function test_ai_분석_요청을_성공적으로_처리한다(): void
public function test_필수_필드_누락_시_유효성_검사가_실패한다(): void
public function test_잘못된_user_id_타입으로_유효성_검사가_실패한다(): void
```

### 필수 테스트 케이스
- **성공 케이스**: 정상적인 요청 처리 확인
- **유효성 검사**: 필수 필드, 타입, 길이 제한 확인
- **데이터베이스**: 저장, 조회, 수정 확인
- **예외 처리**: 에러 상황 적절한 처리 확인

### 테스트 트레이트 사용
```php
use RefreshDatabase, WithFaker;  // 필수 트레이트
```

## 데이터베이스 규칙

### 마이그레이션 규칙
- **기존 마이그레이션 수정 우선**: 새로운 remove/alter 마이그레이션 생성 대신 기존 create 마이그레이션을 직접 수정
- **불필요한 마이그레이션 금지**: 컬럼 추가, 제거가 필요하면 처음부터 해당 컬럼을 생성하지 않는 것이 원칙
- **깔끔한 히스토리 유지**: 개발 단계에서는 마이그레이션 히스토리를 깔끔하게 유지하기 위해 기존 파일 수정을 선호

### 테이블 네이밍
- 복수형 사용: `plobin_document_summary_records`
- 스네이크 케이스 사용
- 접두사 활용: `plobin_` 접두사로 프로젝트 구분

## 프론트엔드 개발 규칙

### 파일 구조 및 네이밍
**절대 원칙**: 모든 프론트엔드 파일은 **무조건** 숫자 접두사 사용
- 올바른 예: `700-page-dashboard.blade.php`, `301-layout-head.blade.php`
- 잘못된 예: `dashboard.blade.php`, `head.blade.php`
- 폴더도 동일: `700-page-sandbox/`, `300-common/`
- 절대 금지: `components/`, `layouts/`, `pages/`, `livewire/`

### 숫자 접두사 체계
- `000-xxx.blade.php`: 인덱스 파일 (메인 레이아웃)
- `100-xxx.blade.php`: 헤더 관련 파일들
- `200-xxx.blade.php`: 메인 콘텐츠, 사이드바 파일들
- `300-xxx.blade.php`: 레이아웃, 모달 파일들
- `400-xxx.blade.php`: JavaScript 파일들 (**필수: 400번대 사용**)
- `500-xxx.blade.php`: AJAX 요청 파일들
- `600-xxx.blade.php`: 데이터 관련 파일들
- `900-xxx.blade.php`: 초기화, 푸터 파일들

### UI 컴포넌트 분리 규칙
**modal, dropdown, table, block 급 컴포넌트는 무조건 파일 분리**
- 올바른 예: `200-modal-user-edit.blade.php`, `200-dropdown-menu.blade.php`, `200-table-users.blade.php`
- 잘못된 예: 큰 파일 안에 모달, 드롭다운, 테이블 코드 섞어 놓기
- **원칙**: 재사용 가능한 모든 UI 컴포넌트는 독립 파일로 분리

**page 급은 무조건 폴더별로 분리**
- 올바른 예: `903-page-users/000-index.blade.php`, `903-page-users/100-header-main.blade.php`
- 잘못된 예: 하나의 파일에 전체 페이지 구조 작성
- **원칙**: 각 페이지는 독립된 폴더에 header, sidebar, content 등으로 분리

### 기술 스택 제한
**순수 JavaScript 사용 금지**
- 사용 금지: Vanilla JS, jQuery, Alpine.js의 복잡한 로직
- 사용 필수: Livewire + Filament 조합만 사용
- 간단한 Alpine.js: 토글, 드롭다운 등 최소한의 UI 상호작용만

## 실제 구현 사례

### AI 문서 분석 요청 API
**엔드포인트**: `POST /api/documents/summaries/analyze`

**구현된 파일들**
- `app/Http/Controllers/DocumentsSummary/Analyze/Controller.php`
- `app/Http/Controllers/DocumentsSummary/Analyze/Request.php`
- `app/Http/Controllers/DocumentsSummary/Analyze/Response.php`
- `tests/Feature/DocumentsSummaryAnalyzeTest.php`

**요청 구조**
```json
{
  "user_id": 1,
  "file_name_with_path": "uploads/session_id/filename.pdf",
  "file_name_only": "filename.pdf",
  "file_path": "uploads/session_id",
  "sandbox_id": "gupsa"
}
```

**응답 구조**
```json
{
  "success": true,
  "message": "AI 분석 요청이 접수되었습니다",
  "data": {
    "request_id": 12345
  }
}
```

**데이터베이스 테이블**: `plobin_document_summary_records`
- 필드: user_id, file_name, file_path, full_file_path, sandbox_id, request_status, requested_at

**콜백 관리**: 최대 5회 재시도 (즉시, 1분, 3분, 15분, 30분 간격), `plobin_document_summary_callback_request_records` 테이블로 상세 추적

## 개발 가이드라인

### 일반 규칙
- 서버가 실행되어있다고 가정하고 진행
- 구현하려는 기능이 이미 있는지 검토
- 말하지 않은 컴포넌트 내용 만들지 않고 내용 비움
- **인증 시스템은 라라벨 기본 Auth + Livewire 사용**: 커스텀 AuthManager 제거됨, Auth::attempt() 등 표준 라라벨 인증 사용
- **파일명 규칙 준수 필수**: 모든 뷰 파일은 숫자 접두사-설명 형식으로 명명

### 주의사항
- **하드코딩 금지**: 실제 데이터나 예시 텍스트를 하드코딩하지 말 것
- **빈 상태 유지**: 요청받은 것만 만들 것. 요청받은 경우 빈 값으로 유지

## 샌드박스 아키텍처 가이드

### 자체 완결형 샌드박스 시스템
**핵심 원칙**: containers.json을 단일 메타데이터 소스로 하는 완전 자체 완결형 샌드박스 아키텍처

**containers.json 구조**
- 모든 샌드박스 컨테이너와 도메인 정보는 `sandbox/container/containers.json`에서 관리
- 하드코딩 절대 금지, 모든 동적 데이터는 containers.json 기반

**도메인별 자체 완결형 구조**
- 각 도메인은 완전히 독립적으로 작동
- 외부 application 로직에 의존 금지
- 데이터베이스 접근, 라우팅, 로직 모두 도메인 내부에서 처리

### 금지 사항
**외부 의존성 금지**
- app/Helpers/ 디렉토리에 샌드박스 관련 헬퍼 생성 금지
- 샌드박스 외부에서 데이터베이스 접근 금지
- 외부 서비스나 컨트롤러에서 샌드박스 로직 처리 금지

**하드코딩 금지**
- 컨테이너명, 도메인명, 스크린명 하드코딩 절대 금지
- 모든 메타데이터는 containers.json에서 동적으로 가져와야 함

## 문서화 규칙

### 기본 원칙
- **반말 사용**: 토큰 절약을 위해 반말로 작성
- **실용성 중심**: 실제 사용 가능한 내용만
- **기존 컨벤션 준수**: 변경 요청 없으면 기존 코드 규칙 따를 것

### 절대 금지사항
- **포트 번호 언급**: 환경별로 다르므로 "로컬 서버", "개발 서버" 등으로 대체
- **중복 문서 작성**: 기존 문서 있는지 먼저 확인
- **코드 예제**: 코드 블록 넣지 말 것 (구현 사례 제외)
- **더미 값**: 요청 외 예시나 더미 데이터 금지
- **메타 정보**: 업데이트 날짜, 작성자, 참고자료 등 금지

## 개발 문서 생성
- php artisan scribe:generate