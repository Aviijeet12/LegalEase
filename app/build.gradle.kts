plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    // Temporarily disabled to fix build - will re-enable after fixing
    // id("kotlin-kapt")
    // id("com.google.dagger.hilt.android")
}

android {
    compileSdk = 34

    namespace = "cm.avisingh.legalease"

    defaultConfig {
        applicationId = "cm.avisingh.legalease"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            //applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

// Kapt settings - disabled since kapt plugin is disabled
// kapt {
//     correctErrorTypes = true
//     useBuildCache = false
//     showProcessorStats = true
// }

dependencies {
    // Core
    implementation("com.google.firebase:firebase-perf-ktx:20.5.1")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // Crashlytics KTX declared with explicit version below; remove unversioned duplicate
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.4")

    // Firebase Dependencies with explicit versions
    implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0") 
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.0")
    implementation("com.google.firebase:firebase-config-ktx:21.6.0")
    
    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Coroutines for Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Retrofit & Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // kapt("androidx.room:room-compiler:2.6.1")

    // DataStore Preferences
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Hilt Dependency Injection - temporarily disabled to fix build
    // implementation("com.google.dagger:hilt-android:2.48.1")
    // kapt("com.google.dagger:hilt-compiler:2.48.1")

    // WorkManager with Hilt
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    // implementation("androidx.hilt:hilt-work:1.1.0")
    // kapt("androidx.hilt:hilt-compiler:1.1.0")

    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.google.crypto.tink:tink-android:1.11.0")

    // View and Animation
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("com.airbnb.android:lottie:6.1.0")
    
    // PDF Viewer - commented out temporarily to fix build
    // implementation("com.github.barteksc:android-pdf-viewer:2.8.2")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}