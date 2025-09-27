rootProject.name = "backend"
include("common")
include("auth-service")
include("user-service")
include("gateway-service")

plugins {
    id("com.gradle.enterprise") version "3.19.2"
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree.set("yes")
        publishing.onlyIf { System.getenv("CI") != null }
    }
}
