plugins {
    id("com.android.application") version "9.0.0" apply false
    id("com.android.library") version "9.0.0" apply false
    id("org.jetbrains.dokka") version "2.1.0" apply false
    id("org.sonarqube") version "7.2.2.6593"
}

sonar {
    properties {
        property("sonar.projectKey", "pcolby_nfc-quick-settings")
        property("sonar.organization", "pcolby")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
