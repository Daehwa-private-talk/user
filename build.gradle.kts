import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.8.22"
    val springBootVersion = "3.0.2"

    id("io.spring.dependency-management") version "1.1.0"
    id("org.springframework.boot") version springBootVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
}

group = "com.daehwa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    val flywayVersion = "9.15.2"
    val springDocVersion = "2.1.0"
    val kotlinLoggingVersion = "3.0.5"
    val jwtVersion = "0.11.5"

    // 기본 설정
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // 시큐리티
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-test")
    implementation("io.jsonwebtoken:jjwt-api:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    // spring doc
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.mysql:mysql-connector-j")
    implementation("org.flywaydb:flyway-mysql:$flywayVersion")
    testImplementation("org.flywaydb:flyway-mysql:$flywayVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

noArg {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
