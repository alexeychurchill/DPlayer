plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("de.mannodermaus.android-junit5")
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "io.alexeychurchill.dplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "io.alexeychurchill.dplayer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    // Kotlin
    val coroutinesGuavaVersion = "1.8.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:$coroutinesGuavaVersion")

    // API Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")

    // Hilt/Dagger
    implementation("com.google.dagger:hilt-android:2.49")
    debugImplementation("androidx.compose.ui:ui-tooling:1.6.8")
    kapt("com.google.dagger:hilt-android-compiler:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // JSON SerDe
    val kotlinJsonVersion = "1.7.1"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinJsonVersion")

    // Android UI
    val materialVersion = "1.12.0"
    implementation("com.google.android.material:material:$materialVersion")

    // Compose
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended:1.6.8")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha14")
    // Jetpack Navigation
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Image Loading/Caching - Glide
    val glideVersion = "4.14.2"
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    val glideComposeVersion = "1.0.0-beta01"
    implementation("com.github.bumptech.glide:compose:$glideComposeVersion")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // Document Framework API
    implementation("androidx.documentfile:documentfile:1.0.1")

    // Media3
    val media3Version = "1.4.1"
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-exoplayer:$media3Version")
    implementation("androidx.media3:media3-session:$media3Version")

    // Unit Testing
    val junitVersion = "5.10.3"
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    val mockkVersion = "1.13.12"
    testImplementation("io.mockk:mockk:$mockkVersion")
    val assertjVersion = "3.25.1"
    testImplementation("org.assertj:assertj-core:$assertjVersion")
}
