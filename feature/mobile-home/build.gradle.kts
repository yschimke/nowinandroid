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
        namespace = "com.google.wear.jetfit.mobile.home"
        minSdk = 26
        targetSdk = 33
    }

    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    kapt(libs.androidx.hilt.hilt.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)

    implementation(projects.core.compose)
    implementation(projects.core.data)
    implementation(projects.core.navigation)

    implementation(libs.com.google.dagger.hilt.android)
}
