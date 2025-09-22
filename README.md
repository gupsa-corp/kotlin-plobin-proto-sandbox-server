# Kotlin Spring Boot SQLite Demo

코틀린과 스프링부트를 사용한 SQLite 연동 데모 프로젝트입니다.

## 기술 스택

- **Kotlin** 1.9.20
- **Spring Boot** 3.2.0
- **Spring Data JPA**
- **SQLite** 3.44.1.0
- **Hibernate Community Dialects** 6.4.0.Final

## 프로젝트 구조

```
src/
├── main/
│   ├── kotlin/com/example/demo/
│   │   ├── DemoApplication.kt           # 메인 애플리케이션 클래스
│   │   ├── entity/
│   │   │   └── User.kt                  # User 엔티티
│   │   ├── repository/
│   │   │   └── UserRepository.kt        # User 리포지토리
│   │   └── controller/
│   │       └── UserController.kt        # User REST 컨트롤러
│   └── resources/
│       └── application.yml              # 애플리케이션 설정
└── test/
    └── kotlin/com/example/demo/
```

## 실행 방법

### 1. 프로젝트 빌드

```bash
./gradlew build
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는

```bash
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### 3. 애플리케이션 접속

브라우저에서 `http://localhost:8080` 접속

## API 엔드포인트

### User API

| 메소드 | URL | 설명 |
|--------|-----|------|
| GET | `/api/users` | 모든 사용자 조회 |
| GET | `/api/users/{id}` | 특정 사용자 조회 |
| POST | `/api/users` | 새 사용자 생성 |
| PUT | `/api/users/{id}` | 사용자 정보 수정 |
| DELETE | `/api/users/{id}` | 사용자 삭제 |
| GET | `/api/users/search?name={name}` | 이름으로 사용자 검색 |

### API 사용 예제

#### 1. 사용자 생성

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "홍길동",
    "email": "hong@example.com",
    "age": 30
  }'
```

#### 2. 모든 사용자 조회

```bash
curl http://localhost:8080/api/users
```

#### 3. 특정 사용자 조회

```bash
curl http://localhost:8080/api/users/1
```

#### 4. 사용자 검색

```bash
curl http://localhost:8080/api/users/search?name=홍
```

## 데이터베이스

- SQLite 데이터베이스는 `./data/app.db` 파일에 저장됩니다
- 애플리케이션 시작 시 자동으로 테이블이 생성됩니다 (DDL-auto: update)
- SQL 로그가 콘솔에 출력됩니다

## 개발 도구

### Gradle Wrapper

```bash
# 의존성 다운로드
./gradlew dependencies

# 테스트 실행
./gradlew test

# 빌드 정리
./gradlew clean
```

## 주요 설정

### SQLite 설정 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:sqlite:./data/app.db
    driver-class-name: org.sqlite.JDBC
  jpa:
    database-platform: org.hibernate.community.dialect.SQLiteDialect
    hibernate:
      ddl-auto: update
    show-sql: true
```