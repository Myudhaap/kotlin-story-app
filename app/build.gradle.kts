@file:Suppress("DEPRECATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)

    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "dev.mayutama.project.storyappsubm"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.mayutama.project.storyappsubm"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev\"")
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
    testOptions {
        animationsDisabled = false
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // lifecycle and viewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    // image load
    implementation(libs.glide)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Timber
    implementation(libs.timber)

    // Exif
    implementation(libs.androidx.exifinterface)

    // lottie
    implementation(libs.lottie)

    // Swipe
    implementation(libs.androidx.swiperefreshlayout)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Paging
    implementation (libs.androidx.paging.runtime)
    implementation(libs.androidx.room.paging)

    // location
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Test
    implementation(libs.androidx.espresso.idling.resource)

    debugImplementation(libs.androidx.fragment.testing)

    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.navigation.testing)


    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    // mock webserver
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)
}