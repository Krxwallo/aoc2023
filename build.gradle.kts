import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-Beta1"
}

group = "io.github.krxwallo"
version = "1.0.0"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.6"

dependencies {
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta9")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.languageVersion = "2.0"
    }

    wrapper {
        gradleVersion = "7.5.1"
    }
}

sourceSets {
    main {
        resources.srcDirs("input")
    }
}
