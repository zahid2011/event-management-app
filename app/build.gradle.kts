plugins {
    id("com.android.application")
//    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {

    tasks.withType<Test>{
        useJUnitPlatform()
    }


    namespace = "com.example.event_lottery"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.event_lottery"
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
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.firebase.database)

    // Android libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.volley)

    // QR Code and image handling
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    testImplementation(libs.testng)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // JUnit and Robolectric for unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.1")
    testImplementation("org.robolectric:robolectric:4.9")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation ("org.mockito:mockito-inline:5.2.0")


    // AndroidX testing libraries
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
