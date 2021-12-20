plugins {
    idea
    kotlin("jvm") version "1.6.10"
}

group = "com.sample"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}
