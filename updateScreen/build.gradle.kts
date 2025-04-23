plugins {
    alias(libs.plugins.ww.main.lib.gradle.plugin)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "ru.profitsw2000.updatescreen"
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    //ViewModel
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.livedata)
    implementation(libs.androidx.viewmodel)
    //Koin
    implementation(libs.koin)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}