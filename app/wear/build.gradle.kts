import org.jetbrains.kotlin.konan.properties.hasProperty
import java.io.FileInputStream
import java.util.*

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        load(FileInputStream(rootProject.file("local.properties")))
    }
}

secrets {
    defaultPropertiesFileName = "defaultsecrets.properties"
}

android {
    namespace = "com.google.wear.jetfit"

    compileSdk = 33

    defaultConfig {
        applicationId = "com.google.wear.jetfit"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    val releaseSigned = localProperties.hasProperty("keyStore")

    if (releaseSigned) {
        signingConfigs {
            create("release") {
                storeFile = file(localProperties["keyStore"] as String)
                keyAlias = localProperties["keyAlias"] as String?
                keyPassword = localProperties["keyPassword"] as String?
                storePassword = localProperties["storePassword"] as String?
            }
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName(if (releaseSigned) "release" else "debug")
        }
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
        freeCompilerArgs += "-opt-in=com.google.android.horologist.tiles.ExperimentalHorologistTilesApi"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.networks.ExperimentalHorologistNetworksApi"
        freeCompilerArgs += "-opt-in=com.google.android.horologist.compose.tools.ExperimentalHorologistComposeToolsApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    packagingOptions {
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/DEPENDENCIES")
    }
}

dependencies {
    implementation("androidx.room:room-common:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    kapt(libs.androidx.hilt.hilt.compiler)
    kapt(libs.com.google.dagger.hilt.compiler)
    kaptTest(libs.com.google.dagger.hilt.android.compiler)
    kaptAndroidTest(libs.com.google.dagger.hilt.android.compiler)

    implementation(projects.feature.wear.tile)
    implementation(projects.feature.wear.home)
    implementation(projects.feature.wear.activity)
    implementation(projects.feature.wear.login)
    implementation(projects.core.compose)
    implementation(projects.data.activities)
    implementation(projects.core.auth)
    implementation(projects.core.charts)
    implementation(projects.core.navigation)
    implementation(projects.feature.wear.complication)

    implementation(libs.startup.runtime)
    implementation(libs.coil)
    implementation(libs.androidx.activity.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.material.icons.core)
    implementation(libs.androidx.compose.material.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.ui.tooling.preview)
    implementation(libs.androidx.core.core.ktx)
    implementation(libs.androidx.hilt.hilt.navigation.compose)
    implementation(libs.androidx.legacy.legacy.support.v4)
    implementation(libs.androidx.lifecycle.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.navigation.compose)
    implementation(libs.androidx.percentlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.wear)
    implementation(libs.androidx.wear.compose.compose.foundation)
    implementation(libs.androidx.wear.compose.compose.material)
    implementation(libs.androidx.wear.compose.compose.navigation)
    implementation(libs.androidx.wear.tiles)
    implementation(libs.com.google.accompanist.accompanist.permissions)
    implementation(libs.com.google.accompanist.accompanist.swiperefresh)
    implementation(libs.com.google.android.gms.play.services.wearable)
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.com.google.guava)
    implementation(libs.com.squareup.okhttp3.okhttp)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.guava)
    implementation(platform(libs.com.squareup.okhttp3.okhttp.bom))
    implementation(libs.horologist.tiles)
    implementation(libs.androidx.wear.tiles.material)
    implementation(libs.horologist.compose.layout)
    implementation(libs.horologist.network.awareness)
    implementation(libs.androidx.metrics.performance)
    implementation(libs.horologist.compose.tools)
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha02")
    implementation(libs.androidx.complications.datasource.ktx)

    debugImplementation(libs.androidx.compose.ui.ui.tooling)

    kaptAndroidTest(libs.com.google.dagger.hilt.android.compiler)
    testAnnotationProcessor(libs.com.google.dagger.hilt.android.compiler)
    testAnnotationProcessor(libs.com.google.dagger.hilt.compiler)
    testImplementation(libs.com.google.dagger.hilt.android.testing)
    testImplementation(libs.junit)
    androidTestAnnotationProcessor(libs.com.google.dagger.hilt.android.compiler)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.espresso.espresso.core)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.com.google.dagger.hilt.android.testing)
    androidTestImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)
    testImplementation(libs.org.assertj.assertj.core)
    androidTestImplementation(libs.org.assertj.assertj.core)
}

fun Any?.writeBuildConfigString(): String =
    if (this != null && this != "") "\"${this}\"" else "null"
