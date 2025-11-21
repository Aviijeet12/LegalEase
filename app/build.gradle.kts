plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'com.google.gms.google-services'
}

android {
    namespace 'com.example.lawclientauth'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.lawclientauth"
        minSdk 23
        targetSdk 34
        versionCode 1
        versionName "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'),
                    'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = '17'
    }

    buildFeatures {
        viewBinding true
    }
}

dependencies {

    // Firebase
    implementation platform('com.google.firebase:firebase-bom:33.1.1')
    implementation 'com.google.firebase:firebase-auth'
    implementation 'com.google.firebase:firebase-firestore'
    implementation 'com.google.firebase:firebase-storage'

    // Material Components
    implementation 'com.google.android.material:material:1.12.0'

    // Glide (for profile images)
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // Biometric authentication
    implementation 'androidx.biometric:biometric:1.2.0-alpha05'

    // Kotlin + Android core dependencies
    implementation 'androidx.core:core-ktx:1.13.1'
    implementation 'androidx.appcompat:appcompat:1.7.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.2.0'
}

