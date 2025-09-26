plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    java
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.mockito:mockito-core")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
