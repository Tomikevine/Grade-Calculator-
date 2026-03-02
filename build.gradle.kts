import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.20"
    id("org.jetbrains.dokka") version "2.0.0"
    application
}

group = "com.gradecalculator"
version = "1.0.0"

application {
    mainClass.set("com.gradecalculator.MainKt")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Apache POI for Excel (.xlsx / .xls)
    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // OpenCSV for CSV parsing and writing
    implementation("com.opencsv:opencsv:5.9")

    // Unit testing
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

// Fat JAR — single executable JAR with all dependencies bundled
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.gradecalculator.MainKt"
    }
    from(configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveFileName.set("grade-calculator.jar")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("docs") {
    group = "documentation"
    description = "Generates HTML API documentation using Dokka."
    dependsOn("dokkaGeneratePublicationHtml")
}
