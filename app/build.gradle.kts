plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.ksp)
	alias(libs.plugins.hilt)
	alias(libs.plugins.jetbrains.kotlin.serialization)
}

android {

	namespace = "com.example.dogbreedschallenge"
	compileSdk = 36

	defaultConfig {
		applicationId = "com.example.dogbreedschallenge"
		minSdk = 24
		targetSdk = 36
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {

		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}

	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}

	buildFeatures {
		compose = true
	}

}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.retrofit)
	implementation(libs.hilt.android)

	// Jetpack Compose
	val composeBom = platform(libs.androidx.compose.bom)
	implementation(composeBom)
	androidTestImplementation(composeBom)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.ui.tooling.preview)
	debugImplementation(libs.androidx.compose.ui.tooling)
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.test.manifest)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.coil.compose)

	// Navigation 3
	implementation(libs.androidx.navigation3.ui)
	implementation(libs.androidx.navigation3.runtime)
	implementation(libs.androidx.lifecycle.viewmodel.navigation3)
	implementation(libs.kotlinx.serialization.core)

	// Room Database
	implementation(libs.androidx.room.runtime)
	implementation(libs.androidx.room.compiler)
	implementation(libs.androidx.room.ktx)

	// Test
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	ksp(libs.hilt.compiler)

}