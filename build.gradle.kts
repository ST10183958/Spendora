// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Firebase / Google services
    id("com.google.gms.google-services") version "4.4.2" apply false

    // KSP (Room compiler)
    id("com.google.devtools.ksp") version "2.3.2" apply false
}