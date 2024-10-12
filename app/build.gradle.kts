plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("com.chaquo.python")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
}

android {
    namespace = "com.TubeMateApp.tubemate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.TubeMateApp.tubemate"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a","armeabi-v7a", "x86_64","x86")
            version =  "3.12.4"
        }
        chaquopy {
            defaultConfig {
                buildPython("C:/Users/parve/AppData/Local//Programs//Python/Python312/python.exe")
                sourceSets {
                    getByName("main") {
                        srcDir("src/main/python")
                    }
                }

                pip{
                    install("yt-dlp")
                    install("instaloader==4.13.1")  // Update the version number as needed
                }
            }


        }

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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    ndkVersion = "3.12.4"
    buildToolsVersion = "34.0.0"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson) // Gson converter (optional, if using Gson for JSON serialization)
    implementation (libs.okhttp) // OkHttp (Retrofit depends on OkHttp)
    implementation (libs.jsoup)


    // for coil img and progressbar
    implementation(libs.msz.progress.indicator)
    implementation(libs.coil.compose)

    // for R8
    /*implementation (libs.bctls.jdk15on)
    implementation (libs.conscrypt.android)
    implementation (libs.openjsse)*/


    implementation(libs.landscapist.glide)



    //implementation (libs.mobile.ffmpeg.full.gpl)
    implementation("com.arthenica:mobile-ffmpeg-min-gpl:4.4.LTS")
    implementation (libs.androidx.core.splashscreen)



    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.2.0"))

    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Messaging
    implementation("com.google.firebase:firebase-messaging")



}