plugins {
    application
    kotlin("jvm")
}

application {
    mainClassName = "LocalizatorApplication"
}

dependencies {
    implementation(project(":data"))
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

repositories {
    jcenter()
}