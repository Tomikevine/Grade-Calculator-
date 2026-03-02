import org.jetbrains.compose.compose
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.compose") version "1.4.0"
    id("org.jetbrains.dokka") version "1.8.20"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    // CSV parsing
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.2.0")
    // Excel reading/writing
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")
}

// Dokka configuration for API documentation
tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        configureEach {
            includes.from("README.md")
            sourceLink {
                localDirectory.set(file("src"))
                remoteUrl.set(java.net.URL("https://github.com/Tomikevine/Grade-Calculator-/tree/main/src"))
                remoteLineSuffix.set("#L")
            }
        }
    }
    outputDirectory.set(file("$buildDir/dokka/html"))
}

tasks.register("dokkaMarkdown", DokkaTask::class) {
    dokkaSourceSets {
        configureEach {
            includes.from("README.md")
        }
    }
    outputDirectory.set(file("$buildDir/dokka/markdown"))
    pluginConfiguration<org.jetbrains.dokka.gradle.DokkaMultiModuleTask, Map<String, String>> {
        put("org.jetbrains.dokka.gradle.GfmPlugin", "{}")
    }
}

compose.desktop {
    application {
        mainClass = "com.example.gradcalc.MainKt"
        nativeDistributions {
            targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb)
            packageName = "GradeCalculator"
            packageVersion = "1.0.0"
        }
    }
}
