import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    application
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

application {
    mainClassName = "LocalizatorApplication"
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

repositories {
    jcenter()
}

kapt {
    correctErrorTypes = true
}

sourceSets {
    named("main") {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("src/main/kotlin")
        }
    }
    named("test") {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("src/test/kotlin")
        }
    }
}