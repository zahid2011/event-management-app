plugins {
    id("com.android.application")
//    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
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


    packagingOptions {
        exclude("META-INF/LICENSE.md")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/LICENSE-notice.md")  // Adding this specific file
        exclude("META-INF/DEPENDENCIES")
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

    tasks.withType<Test>{
            useJUnitPlatform()
        }

    lint {
        abortOnError = false
    }


    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.espresso.core)
    implementation(libs.junit.jupiter)
    implementation(libs.junit.jupiter)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")


    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")

    testImplementation("org.robolectric:robolectric:4.9.2")

    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Unit Testing

    testImplementation("io.mockk:mockk:1.13.5")
    // UI and Instrumentation Testing
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")


    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test:rules:1.5.0")




    androidTestImplementation("com.google.truth:truth:1.1.3")
    androidTestImplementation ("io.mockk:mockk-android:1.12.0")

    implementation("androidx.camera:camera-core:1.4.0")
    implementation("androidx.camera:camera-view:1.4.0")
    implementation("androidx.camera:camera-lifecycle:1.4.0")
    implementation("androidx.camera:camera-extensions:1.4.0")


}