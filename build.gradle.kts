// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.1") // Gradle Android plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10") // Kotlin plugin
        classpath("com.google.gms:google-services:4.3.10") // Google Services plugin for Firebase
    }
    repositories {
        // ... other repositories
        google()
    }
}
