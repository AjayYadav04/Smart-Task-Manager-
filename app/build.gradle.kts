plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt") // For Room annotation processing
    id("com.google.gms.google-services")}

android {
    namespace = "com.example.smarttaskmanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smarttaskmanager"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
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

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    // AndroidX Libraries
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0")
    implementation("androidx.room:room-runtime:2.4.0")
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.material)
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation(libs.androidx.activity)
    implementation(libs.firebase.messaging)
    implementation(libs.core.ktx)
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    kapt("androidx.room:room-compiler:2.6.0")

    // Biometric
    implementation ("androidx.biometric:biometric:1.2.0-alpha05")

    // Retrofit for Network Requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Glide for Image Loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    // Google Maps SDK
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    // Firebase Cloud Messaging (Optional for push notifications)
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging-directboot:20.2.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

}
