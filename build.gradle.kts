import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // SQLite dependencies
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.0.Final")

    // Swagger dependencies
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // File processing dependencies
    implementation("org.apache.commons:commons-compress:1.26.0")

    // Validation dependencies
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // SSH dependencies
    implementation("com.jcraft:jsch:0.1.55")

    // WebSocket dependencies
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Explicit test framework dependencies to avoid deprecation warnings
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}