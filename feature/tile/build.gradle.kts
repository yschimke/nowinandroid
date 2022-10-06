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
        namespace = "com.google.wear.onestep.wear.tile"
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
        freeCompilerArgs += "-opt-in=com.google.android.horologist.tiles.ExperimentalHorologistTilesApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation(projects.core.compose)

    kapt(libs.androidx.hilt.hilt.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)

    implementation(libs.startup.runtime)
    implementation(libs.androidx.compose.material.material.icons.core)
    implementation(libs.androidx.compose.material.material.icons.extended)
    implementation(libs.androidx.wear.tiles)
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.horologist.tiles)
    implementation(libs.androidx.wear.tiles.material)
}
