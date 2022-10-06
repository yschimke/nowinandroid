@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 33

    defaultConfig {
        namespace = "com.google.wear.onestep.core.navigation"
        minSdk = 26
        targetSdk = 33
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    kapt(libs.androidx.hilt.hilt.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)

    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.androidx.wear.compose.compose.navigation)
}
