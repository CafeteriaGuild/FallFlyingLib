# FallFlyingLib was superseded by the official Elytra events available at Fabric Entity API.

# FallFlyingLib

[![](https://jitpack.io/v/adriantodt/FallFlyingLib.svg)](https://jitpack.io/#adriantodt/FallFlyingLib) [![Curseforge](http://cf.way2muchnoise.eu/title/fallflyinglib.svg)](https://www.curseforge.com/minecraft/mc-mods/fallflyinglib)

A lightweight library to provide compatibility between mods that implement Elytra alternatives.

Bad Mixins and Callbacks might make a Elytra alternative stop working. This library exists so that all modifications can
be made from a single place.

This library ~~takes inspiration~~ is implemented using
the [PlayerAbilityLib](https://github.com/Ladysnake/PlayerAbilityLib) API.

Use `FallFlyingLib.ABILITY` the same way as you would for creative flight!

## Adding FFL to your project

You can add the library by inserting the following in your `build.gradle` :

```gradle
repositories {
    maven {
        url = 'https://maven.cafeteria.dev'
        content {
            includeGroup 'net.adriantodt.fabricmc'
        }
    }
    maven {
        name = 'Ladysnake Mods'
        url = 'https://ladysnake.jfrog.io/artifactory/mods'
        content {
            includeGroup 'io.github.ladysnake'
            includeGroupByRegex 'io\\.github\\.onyxstudios.*'
        }
    }
}

dependencies {
    modImplementation "net.adriantodt.fabricmc:fallflyinglib:${project.ffl_version}"
    include "net.adriantodt.fabricmc:fallflyinglib:${project.ffl_version}"
}
```

You can then add the library version to your `gradle.properties`file:

```properties
# FallFlyingLib
ffl_version=2.x.y
```

You can find the current version of FFL in the [releases](https://github.com/adriantodt/FallFlyingLib/releases) tab of
the repository on Github.

Be sure to also add PlayerAbilityLib!

## "XYZ mod is incompatible with FFL"

Any mods that mixin into the parts of the code responsible for Elytra Flight will NOT work.

Ask the developer to use FallFlyingLib instead. It's really easy to implement things that do Elytra Flight, and there's
also events implemented by FallFlyingLib that mods can listen.

And if the latter isn't enough, please feel free to contribute with the necessary events your mod needs!
