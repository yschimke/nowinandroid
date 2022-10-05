@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.kapt) apply false
    alias(libs.plugins.dagger.hilt.android.plugin) apply false
    alias(libs.plugins.ksp) apply false
}

tasks.create<Delete>("clean") {
    group = "build"
    delete = setOf (
        rootProject.buildDir
    )
}
