plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.d1_jetpackcompose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.d1_jetpackcompose"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // --- CORE & LIFECYCLE ---
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // --- COMPOSE ---
    // Bill of Materials (BOM) untuk memastikan versi Compose konsisten
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material3)
    // Preview & Tooling (hanya untuk build debug)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // --- NAVIGATION ---
    // Dependensi untuk navigasi dasar (non-compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    // SOLUSI: HANYA GUNAKAN SATU DECLARASI INI untuk Navigation-Compose
    implementation(libs.androidx.navigation.compose)

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // BOM untuk testing Compose
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}