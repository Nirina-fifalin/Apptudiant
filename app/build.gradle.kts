plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.aptudiant"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aptudiant"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    implementation(libs.recyclerview)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.gson)

    implementation(libs.volley)
    implementation (libs.mpandroidchart)

    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}