import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsFeature

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

listOf(
    "commonConfiguration.gradle",
    "libraryConfiguration.gradle"
).forEach { file ->
    apply(from = "${rootProject.projectDir}/gradle/${file}")
}

androidExtensions {
    isExperimental = true
    features = setOf(AndroidExtensionsFeature.PARCELIZE.featureName)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
    implementation(project(":model"))

    implementation(Dep.Kotlin.stdlibJvm)

    testImplementation(Dep.Test.junit)
}

//apply(from = "../publish_local.gradle")