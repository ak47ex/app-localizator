plugins {
    kotlin("jvm") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

allprojects {
    group = "com.suenara.localizator"
    version = "1.1.0"

    repositories {
        jcenter()
    }
}


sourceSets {
    named("main") {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/main/kotlin")
        }
    }
    named("test") {
        withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
            kotlin.srcDir("src/test/kotlin")
        }
    }
}

kapt {
    correctErrorTypes = true
}