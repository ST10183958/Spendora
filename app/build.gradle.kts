plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")

    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.menak.login"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.menak.login"
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
    dependencies {

        // Core Android
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.activity:activity-compose:1.9.2")

        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        implementation("androidx.datastore:datastore-preferences:1.1.1")

        // Compose BOM
        implementation(platform("androidx.compose:compose-bom:2024.06.00"))

        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.foundation:foundation")

        // Navigation + ViewModel
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
        implementation("androidx.navigation:navigation-compose:2.8.0")
        implementation("androidx.compose.material:material-icons-extended")

        // ROOM
        implementation("androidx.room:room-runtime:2.7.2")
        implementation("androidx.room:room-ktx:2.7.2")
        ksp("androidx.room:room-compiler:2.7.2")

        // MPAndroidChart
        implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

        // FIREBASE
        implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.firebase:firebase-firestore-ktx")
        implementation("com.google.firebase:firebase-analytics-ktx")

        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

        // Testing
        testImplementation("junit:junit:4.13.2")
        testImplementation("androidx.room:room-testing:2.7.2")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("androidx.arch.core:core-testing:2.2.0")

        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")

        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
    }
}