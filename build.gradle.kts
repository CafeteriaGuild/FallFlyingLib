import com.jfrog.bintray.gradle.BintrayExtension.PackageConfig
import java.text.MessageFormat.format as messageFormat

plugins {
    id("fabric-loom") version "0.4-SNAPSHOT"
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.5"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

version = project.property("mod_version").toString()
group = project.property("maven_group").toString()
project.setProperty("archivesBaseName", project.property("archives_base_name"))

dependencies {
    //to change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${project.property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_version")}")
}

tasks.processResources {
    inputs.property("version", project.version)

    from(sourceSets["main"].resources.srcDirs) {
        include("fabric.mod.json")
        expand("version" to project.version)
    }
    from(sourceSets["main"].resources.srcDirs) {
        exclude("fabric.mod.json")
    }
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

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
            // add all the jars that should be included when publishing to maven
            artifact(tasks.remapJar.get()) {
                builtBy(tasks.remapJar)
            }
            artifact(sourcesJar) {
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
    // select the repositories you want to publish to
    repositories {
        mavenLocal()
    }
}

tasks.create("printInfo") {
    doLast {
        println(messageFormat(
            """
            ----- BEGIN -----
            ### Release Info
            This release was built for Minecraft **{0}**, with Fabric Loader **{1}** and Fabric API **{2}**
            ------ END ------""".trimIndent(),
            project.property("minecraft_version"),
            project.property("loader_version"),
            project.property("fabric_version")
        ))
    }
}

fun findProperty(s: String) = project.findProperty(s) as String?
bintray {
    user = findProperty("bintrayUsername")
    key = findProperty("bintrayApiKey")
    publish = true
    setPublications("mavenJava")
    pkg(delegateClosureOf<PackageConfig> {
        repo = "maven"
        name = findProperty("archives_base_name")
        setLicenses("MIT")
        vcsUrl = "https://github.com/adriantodt/FallFlyingLib.git"
    })
}
tasks.bintrayUpload {
    dependsOn("build", "publishToMavenLocal")
}