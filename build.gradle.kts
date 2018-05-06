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

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("http://maven.sk89q.com/repo/")
}

dependencies {
    implementation("org.apache.tomcat:tomcat-jdbc:7.0.52")

    compileOnly("org.spigotmc:spigot-api:1.12-R0.1-SNAPSHOT")
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
    relocate("org.apache.tomcat.jdbc", "com.helion3.prism.libs.org.apache.tomcat.jdbc")
    relocate("org.apache.juli", "com.helion3.prism.libs.org.apache.juli")
}