plugins {
    id("java")
    id("maven-publish")
}

// TODO: Replace project properties with your own properties.
group = "top.faved.particlelib"
version = "1.0-SNAPSHOT"
description = "A plugin which adds a Particle API."

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

// TODO: Set the version to the version you want to develop against.
val mcVer = "1.21.4"

dependencies {
    compileOnly("io.papermc.paper:paper-api:$mcVer-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "favedReleases"
            url = uri("https://maven.faved.top/releases")
            credentials {
                username = project.property("favedReleasesUsername") as? String
                password = project.property("favedReleasesPassword") as? String
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = "top.faved"
            artifactId = "particlelib"
            version = project.version.toString()
            from(components["java"])
        }
    }
}