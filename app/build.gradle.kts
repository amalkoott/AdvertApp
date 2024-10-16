plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "ru.amalkoott.advtapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.amalkoott.advtapp"
        minSdk = 27
        targetSdk = 33
        versionCode = 2
        versionName = "1.1-alpha"

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
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        //sourceCompatibility = JavaVersion.VERSION_1_8
        //targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        //jvmTarget = "1.8"
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        // ...
        checkAllWarnings = true
        ignoreWarnings = true
    }
    // Allow references to generated code
    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    //implementation("androidx.core:core:2.2.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.datastore:datastore-preferences-core-jvm:1.1.1")
    /*
    implementation("com.android.tools.compose:compose-preview-renderer:0.0.1-alpha01"){
        exclude(module = "compose-preview-renderer-0.0.1-alpha01")
    }

     */

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // notification
    implementation("com.google.accompanist:accompanist-permissions:0.31.1-alpha")

    // for image slider
    //implementation("io.coil-kt:coil-compose:2.3.0")
    implementation("com.google.accompanist:accompanist-pager:0.22.0-rc")
    implementation("io.coil-kt:coil-compose:2.6.0")

            // for many view models
    implementation("androidx.fragment:fragment-ktx:1.3.0")

    // for NavHost
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // room

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")


    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    // retrofit
    val retrofit_version = "2.9.0"
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // WorkManager dependency
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.0.0")

    // calendar
    implementation("androidx.compose.material3:material3:1.3.0-alpha06")

    // images
   // implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    // dagger и hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-android-compiler:2.50")

    // When using Kotlin.
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // For Preferences DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")


    //implementation("androidx.datastore:datastore:2.3.1")
    /*
        // Splash API
        implementation ("androidx.core:core-splashscreen:1.0.1")
    */
}