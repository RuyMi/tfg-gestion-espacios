import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.spring") version "1.8.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("org.jetbrains.dokka") version "1.8.10"
}

group = "es.dam"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    // MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //Spring WebSocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Serialización
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // Test
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.mockk:mockk:1.13.4")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")

    // Mockk
    testImplementation("com.ninja-squad:springmockk:4.0.2")

    // Spring security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT
    implementation("com.auth0:java-jwt:4.2.1")
}

dependencyManagement {
    imports {
        mavenBom("com.microsoft.azure:azure-spring-boot-bom:2.3.0")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}