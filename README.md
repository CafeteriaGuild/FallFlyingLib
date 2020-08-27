# FallFlyingLib

[![](https://jitpack.io/v/adriantodt/FallFlyingLib.svg)](https://jitpack.io/#adriantodt/FallFlyingLib) [![Curseforge](http://cf.way2muchnoise.eu/title/fallflyinglib.svg)](https://www.curseforge.com/minecraft/mc-mods/fallflyinglib) 

A lightweight library to provide compatibility between mods that implement Elytra alternatives.

Bad Mixins and Callbacks might make a Elytra alternative stop working. This library exists so that all modifications
can be made from a single place.

This library takes inspiration in PlayerAbilityLib.

## Adding FFL to your project

You can add the library by inserting the following in your `build.gradle` :

```gradle
repositories {
    maven {
        name = "AdrianTodt's Maven"
        url = "https://dl.bintray.com/adriantodt/maven"
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
ffl_version = 1.x.y
```

You can find the current version of FFL in the [releases](https://github.com/adriantodt/FallFlyingLib/releases) tab of the repository on Github.

## Using FFL

Use the method `FallFlyingLib.registerAccessor` to register a path to access a `FallFlyingAbility` instance.
Note that FallFlyingLib only uses it, and it is up to the developer to implement how and if this interface will be stored.
(Developer Note: You should take a look at [Cardinal Components API](https://github.com/OnyxStudios/Cardinal-Components-API).)

The `FallFlyingAbility` will be then pooled if flight is allowed.