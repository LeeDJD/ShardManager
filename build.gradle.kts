plugins {
    java
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "space.kappes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
                "Main-Class" to "space.kappes.Shardmanager.Launcher"
        ))
    }
}

dependencies {
    implementation("log4j", "log4j", "1.2.17")
    implementation("org.slf4j", "slf4j-nop", "1.8.0-beta2")
    implementation("org.json", "json", "20180130")
    testImplementation("junit", "junit", "4.12")
}