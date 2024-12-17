// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" // Ksp (Kotlin symbolic processor) for generating code from annotation for room and hilt,...
    id("com.google.dagger.hilt.android") version "2.50" apply false // Hilt
}