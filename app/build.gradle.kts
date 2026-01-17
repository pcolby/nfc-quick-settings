import java.io.FileInputStream
import java.util.Properties

plugins {
    id("base")
    id("com.android.application")
    id("org.jetbrains.dokka")
}

// Optionally, load keystore properties from a file specified via the environment.
val keystoreProperties = Properties()
val keystorePropertiesFile: String? = System.getenv("KEYSTORE_PROPERTIES_FILE")
if (keystorePropertiesFile != null) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        if (keystorePropertiesFile != null) {
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as? String
                keyPassword = keystoreProperties["keyPassword"] as? String
                storeFile = file(keystoreProperties["storeFile"]!!)
                storePassword = keystoreProperties["storePassword"] as? String
            }
        }
    }
    namespace = "au.id.colby.nfcquicksettings"
    compileSdk = 36

    defaultConfig {
        applicationId = "au.id.colby.nfcquicksettings"
        minSdk = 24
        targetSdk = 36
        versionCode = 19
        versionName = "1.5.3-pre"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            resValue("string", "application_id", "${defaultConfig.applicationId}")
            resValue(
                "string", "build_version",
                "${defaultConfig.versionName}+${defaultConfig.versionCode}.${System.getenv("BUILD_ID") ?: "local"}.debug"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            ndk { debugSymbolLevel = "FULL" }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
            resValue("string", "application_id", "${defaultConfig.applicationId}")
            resValue(
                "string", "build_version",
                "${defaultConfig.versionName}+${defaultConfig.versionCode}.${System.getenv("BUILD_ID") ?: "local"}"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

base {
    archivesName = "${android.defaultConfig.applicationId?.replaceFirst(Regex("^.*\\."), "")}-" +
            "${android.defaultConfig.versionName}+${android.defaultConfig.versionCode}" +
            ".${System.getenv("BUILD_ID") ?: "local"}"
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}
