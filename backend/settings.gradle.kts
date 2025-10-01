rootProject.name = "backend"
include("discovery-service")
include("common")
include("auth-service")
include("user-service")
include("gateway-service")

plugins {
    id("com.gradle.develocity") version "4.1.1"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        publishing.onlyIf { System.getenv("CI") != null }
    }
}
