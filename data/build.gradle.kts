plugins {
    alias(libs.plugins.ww.main.lib.gradle.plugin)
}

android {
    namespace = "ru.profitsw2000.data"
}

dependencies {

    implementation(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    //Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    //ViewModel
    implementation(libs.androidx.livedata)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}