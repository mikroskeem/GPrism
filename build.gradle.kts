import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "2.0.2"
}

group = "eu.mikroskeem"
version = "1.12-2.0.12"

repositories {
    mavenLocal()
    mavenCentral()

    maven("https://repo.destroystokyo.com/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("http://maven.sk89q.com/repo/")
}

dependencies {
    implementation("com.zaxxer:HikariCP:3.1.0") {
        exclude(module = "slf4j-api") // Present in Paper
    }

    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.sk89q:worldedit:6.0.0-SNAPSHOT")
}

val processResources by tasks.getting(ProcessResources::class) {
    val tokens = mapOf(
            "project.name" to "Prism",
            "project.version" to "$version",
            "project.description" to "Handles event tracking, rollbacks, restores and grief management tools"
    )

    filesMatching("plugin.yml") {
        filter { str ->
            var newStr = str
            tokens.forEach { element -> newStr = newStr.replace('$' + "{${element.key}}", element.value) }
            return@filter newStr
        }
    }
}

val shadowJar by tasks.getting(ShadowJar::class) {
    relocate("com.zaxxer.hikari", "com.helion3.prism.libs.com.zaxxer.hikari")
}