import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.0"
}

group = "com.math"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "ProgramForMisha"
            packageVersion = "1.1.0"
            version = "1.1.0"
            description = "Поиск минимальной ширины прямоугольников"
            copyright = "© 2023 Dragontino. All rights reserved."

            windows {
                iconFile.set(project.file("icons/app_icon.ico"))
                packageVersion = "1.1.0"
                version = "1.1.0"
                installationPath = "C:\\Users\\petro\\Downloads"
                dirChooser = true
                perUserInstall = true
            }
        }
    }
}
