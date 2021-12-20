import com.google.protobuf.gradle.*

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
            mavenCentral()
        }
    }
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.8.18")
    }
}

plugins {
    idea
    kotlin("jvm") version "1.6.10"
    id("com.google.protobuf") version "0.8.18"
}

group = "com.sample.grpc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.19.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.42.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach { task ->
            task.plugins {
                id("grpc")
            }
            task.builtins {
                create("js")
            }
        }
    }
}

idea {
    module {
        generatedSourceDirs.add(file("${protobuf.protobuf.generatedFilesBaseDir}/main/grpc"))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    implementation("ch.qos.logback:logback-classic:1.2.9")

    implementation("io.grpc:grpc-netty-shaded:1.43.0")
    implementation("io.grpc:grpc-protobuf:1.42.1")
    implementation("io.grpc:grpc-stub:1.43.0")

    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

    testImplementation("io.kotest:kotest-runner-junit5:5.0.2")
}
