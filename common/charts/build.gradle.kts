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
        namespace = "com.google.wear.jetfit.core.charts"
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
        freeCompilerArgs += "-opt-in=com.google.android.horologist.compose.navscaffold.ExperimentalHorologistComposeLayoutApi"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.compose.tools.ExperimentalHorologistComposeToolsApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    kapt(libs.androidx.hilt.hilt.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)

    implementation(projects.common.compose)
    implementation(projects.usecase.reports)

    implementation(libs.androidx.wear.compose.compose.foundation)
    implementation(libs.androidx.wear.compose.compose.material)
    implementation(libs.androidx.activity.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.core.core.ktx)
    implementation(libs.com.google.dagger.hilt.android)

    debugImplementation(libs.horologist.compose.tools)
    debugImplementation(libs.androidx.compose.ui.ui.tooling.preview)
}
