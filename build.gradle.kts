plugins {
    id("com.android.application") version "9.2.1" apply false
    id("com.android.library") version "9.2.1" apply false
    id("org.jetbrains.dokka") version "2.2.0" apply false
    id("org.sonarqube") version "7.3.0.8198"
}

sonar {
    properties {
        property("sonar.projectKey", "pcolby_nfc-quick-settings")
        property("sonar.organization", "pcolby")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
