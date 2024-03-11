plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "app.android.heartrate.phoneapp"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "app.android.heartrate.phoneapp"
        minSdk = 28
        targetSdk = 34
        versionCode = 7
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

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity:1.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.yehiahd:FastSave-Android:1.0.6")
    implementation ("com.yalantis:ucrop:2.2.0")
    implementation ("com.airbnb.android:lottie:6.1.0")


    implementation ("io.github.ShawnLin013:number-picker:2.4.13")

    implementation ("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

    implementation ("com.daimajia.androidanimations:library:2.4@aar")
    implementation ("de.psdev.licensesdialog:licensesdialog:1.8.1")
    implementation ("com.github.owl-93.Determinate-Progress-View:release:v1.4.4")
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
    implementation ("com.google.android.gms:play-services-ads:21.4.0")
}