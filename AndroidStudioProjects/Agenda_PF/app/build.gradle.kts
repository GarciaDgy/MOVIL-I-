plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.agenda_pf"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.agenda_pf"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    // Tus dependencias originales
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Dependencias de navegación
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha10")
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Jetpack Compose Material3 para resolver 'topAppBarColors'
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation(libs.androidx.material3)

    // Dependencias de pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependencias de Compose para AndroidTest
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging para Compose
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Agregadas para asegurar la compatibilidad completa con Material3 y Compose
    implementation("androidx.compose.ui:ui:1.4.0") // Versión estable de UI Compose
    implementation("androidx.compose.ui:ui-tooling-preview:1.4.0") // Previews de Compose
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.0") // Herramientas de Debug para UI Compose
    implementation("io.coil-kt:coil-compose:2.0.0")
}
