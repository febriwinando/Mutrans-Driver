plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("realm-android")

}

android {
    signingConfigs {
        getByName("debug") {
            storeFile =
                file("/Users/diskominfo/Documents/Aplikasi Android/Mutrans/MUTRANSDriver/keydriver.jks")
            storePassword = "diskominfo"
            keyAlias = "keydriver"
            keyPassword = "diskominfo"
        }
        create("release") {
            storeFile =
                file("/Users/diskominfo/Documents/Aplikasi Android/Mutrans/MUTRANSDriver/keydriver.jks")
            storePassword = "diskominfo"
            keyAlias = "keydriver"
            keyPassword = "diskominfo"
        }
    }
    namespace = "go.mutrans.driver"
    compileSdk = 34

    defaultConfig {
        applicationId = "go.mutrans.driver"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.jakewharton.picasso:picasso2-okhttp3-downloader:1.1.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("org.jsoup:jsoup:1.18.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.google.firebase:firebase-analytics:22.1.2")
    implementation("com.google.firebase:firebase-analytics:22.1.2")
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.1")
    implementation("com.google.firebase:firebase-messaging:24.1.0")
    implementation("com.google.firebase:firebase-iid:21.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    //    GMS
    implementation("com.google.android.libraries.places:places:4.1.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.1.0")
//    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.maps:google-maps-services:2.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.9.0")

    implementation("me.relex:circleindicator:2.1.6")
    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("org.greenrobot:eventbus:3.3.1")
    implementation("io.github.florent37:shapeofview:1.4.7")
    implementation("io.realm:realm-gradle-plugin:10.19.0")

    implementation("io.github.everythingme:overscroll-decor-android:1.1.1")

}