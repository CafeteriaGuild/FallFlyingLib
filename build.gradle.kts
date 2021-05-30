import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import java.text.MessageFormat.format as messageFormat

plugins {
    id("fabric-loom") version "0.8-SNAPSHOT"
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

version = project.property("mod_version").toString()
group = project.property("maven_group").toString()
base { archivesBaseName = property("archives_base_name").toString() }

repositories {
    maven {
        name = "Ladysnake Mods"
        url = uri("https://ladysnake.jfrog.io/artifactory/mods")
        content {
            includeGroup("io.github.ladysnake")
            includeGroupByRegex("io\\.github\\.onyxstudios.*")
        }
    }
}

dependencies {
    //to change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")

    modImplementation("io.github.ladysnake:PlayerAbilityLib:1.3.0-nightly.1.17-pre1")
    include("io.github.ladysnake:PlayerAbilityLib:1.3.0-nightly.1.17-pre1")
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    //options.release = 16
}


val remapJar = tasks.getByName<RemapJarTask>("remapJar")
val remapSourcesJar = tasks.getByName<RemapSourcesJarTask>("remapSourcesJar")

// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
// if it is present.
// If you remove this task, sources will not be generated.
val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

tasks.jar {
    from("LICENSE")
}

// configure the maven publication
publishing {
    publications {
        create("mavenJava", MavenPublication::class.java) {

            groupId = project.group.toString()
            artifactId = project.name.toLowerCase()
            version = project.version.toString()

            // add all the jars that should be included when publishing to maven
            artifact(remapJar) {
                builtBy(remapJar)
            }
            artifact(sourcesJar) {
                builtBy(remapSourcesJar)
            }
        }
    }
    // select the repositories you want to publish to
    repositories {
        maven {
            url = uri("https://maven.cafeteria.dev/releases")

            credentials {
                username = "${project.property("mcdUsername")}"
                password = "${project.property("mcdPassword")}"
            }
            authentication {
                create("basic", BasicAuthentication::class.java)
            }
        }
        mavenLocal()
    }
}

tasks.create("printInfo") {
    doLast {
        println(
            messageFormat(
                """
            ----- BEGIN -----
            ### Release Info
            This release was built for Minecraft **{0}**, with Fabric Loader **{1}** and Fabric API **{2}**
            ------ END ------""".trimIndent(),
                project.property("minecraft_version"),
                project.property("loader_version"),
                project.property("fabric_version")
            )
        )
    }
}
