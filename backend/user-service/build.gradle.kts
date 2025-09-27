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

// 🔹 Déclaration du nouveau sourceSet pour integrationTest
sourceSets {
    create("integrationTest") {
        java {
            compileClasspath += sourceSets["main"].output + sourceSets["test"].output
            runtimeClasspath += output + compileClasspath
            srcDir("src/integrationTest/java")
        }
        resources.srcDir("src/integrationTest/resources")
    }
}

// 🔹 Configurations héritées des tests unitaires
configurations {
    named("integrationTestImplementation") {
        extendsFrom(configurations.testImplementation.get())
    }
    named("integrationTestRuntimeOnly") {
        extendsFrom(configurations.testRuntimeOnly.get())
    }
}

// 🔹 Tâche Gradle dédiée
tasks.register<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    shouldRunAfter(tasks.test)
    useJUnitPlatform()
}
