plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
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

    // --- CORE & UTILS ---
    implementation(libs.coil.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.compose.material.icons.extended)

    // --- COMPOSE BOM (Bill of Materials) ---
    // Menggunakan versi terbaru Desember 2025 untuk mendukung fitur animasi paling stabil
    implementation(platform(libs.androidx.compose.bom.v20251200))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // --- ANIMATION & INTERACTIVITY ---
    // Pustaka eksplisit untuk memastikan fitur animasi transisi berjalan lancar
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.foundation)

    // Lottie untuk animasi kompleks (seperti loading 'orang berlari' atau checkmark)
    implementation(libs.lottie.compose)

    // --- SPLASH SCREEN & STARTUP ---
    // Digunakan untuk menangani loading awal saat aplikasi dibuka (isCheckingSession)
    implementation(libs.androidx.core.splashscreen)

    // --- NAVIGATION ---
    // Diperbarui ke versi yang mendukung transisi halaman secara bawaan
    implementation(libs.androidx.navigation.compose.v285)

    // --- ROOM DATABASE ---
    val roomVersion = "2.6.1" // Versi stabil terbaru untuk Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.bom.v20251201)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Networking: Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// JSON Converter: Mengubah JSON dari API menjadi Object Kotlin secara otomatis
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// Logging: Untuk melihat log data API di Logcat (Sangat membantu proses debugging)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
// Image Loading: Coil (Anda sudah memakainya, pastikan versinya terbaru)
    implementation("io.coil-kt:coil-compose:2.5.0")
}
