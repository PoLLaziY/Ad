plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.geks.adtest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.geks.adtest"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildFeatures.buildConfig = true
        buildConfigField ("String", "ADMOB_OPEN_ID",     "\"ca-app-pub-3940256099942544/3419835294\"")
        buildConfigField ("String", "ADMOB_INTER_ID",    "\"ca-app-pub-3940256099942544/1033173712\"")
        buildConfigField ("String", "ADMOB_BANNER_ID",   "\"ca-app-pub-3940256099942544/6300978111\"")
        buildConfigField ("String", "ADMOB_REWARDED_ID", "\"ca-app-pub-3940256099942544/5224354917\"")
        buildConfigField ("String", "YANDEX_INTER_ID",   "\"R-M-DEMO-interstitial\"")
        buildConfigField ("String", "YANDEX_BANNER_ID",  "\"R-M-DEMO-320x50\"")
        buildConfigField ("String", "YANDEX_REWARDED_ID","\"R-M-338228-6\"")
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
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //Advert
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("com.google.ads.mediation:adcolony:4.8.0.2")
    implementation("com.google.ads.mediation:applovin:11.11.3.0")
    implementation("com.google.ads.mediation:facebook:6.16.0.0")
    implementation("com.google.ads.mediation:vungle:6.12.1.1")
    implementation("com.google.ads.mediation:mintegral:16.5.11.0")
    implementation("com.google.ads.mediation:pangle:5.4.1.1.0")
    implementation("com.google.ads.mediation:tapjoy:13.1.2.0")
    implementation("com.unity3d.ads:unity-ads:4.8.0")
    implementation("com.google.ads.mediation:unity:4.8.0.0")
    //Advert Yandex
    implementation("com.yandex.android:mobileads:6.0.1")
    implementation("com.yandex.ads.mediation:mobileads-mytarget:5.18.0.0")


    implementation ("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
}