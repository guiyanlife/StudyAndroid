plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace  = "com.github.studyandroid.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.github.studyandroid.app"
        minSdk        = 23
        targetSdk     = 36
        versionCode   = 1
        versionName   = "1.0"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false // 不混淆
        }
        release {
            isMinifyEnabled   = true // 混淆
            isShrinkResources = true // 资源压缩
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
    kotlin {
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true // 启用 BuildConfig，用于 BuildConfig.DEBUG 等条件编译
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas") // Room schema 导出目录，配合 exportSchema = true 生成版本 diff 文件。文件路径：app/schemas/com.github.studyandroid.app.data.local.AppDatabase/<version>.json
        arg("room.incremental", "true")
    }
}

dependencies {
    // Core
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Compose BOM（统一管理所有 Compose 版本）
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Navigation Compose（Type-Safe）
    implementation(libs.androidx.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit + OkHttp + Kotlin Serialization
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.kotlin.serialization)

    // Coil（图片加载）
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Local HTTP Server
    implementation(libs.nanohttpd)
}
