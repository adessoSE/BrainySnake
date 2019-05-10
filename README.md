[![CircleCI](https://circleci.com/gh/adessoAG/BrainySnake.svg?style=shield)](https://circleci.com/gh/adessoAG/BrainySnake)

# BrainySnake
![brainySnakePic](/Dokumentation/brainySnake.png)

BrainySnake is a game based on the all time classic game "Snake".
 In BrainySnake you don't actively control your snake but instead program its behaviour so it follows certain patterns.
During a game, multiple autonomous snakes are loaded simultaneously and start battling for victory!
 After 1000 moves (number is adjustable in config), whoever is still alive and has the most points, wins.

# IDE configuration

## Build Project with IntelliJ
1. Checkout project
1. Run `gradlew idea` or for reinit `gradlew cleanidea idea` in your favorite console
(On Unix `./gradlew cleanidea idea`)
1. Open the generated `.ipr` with IntelliJ file or `gradlew desktop:run` (On Unix `./gradlew run`)

## Create Run Configuration in IntelliJ
1. Create a new Run Configuration of type _Application_
1. Set `de.adesso.brainysnake.desktop.DesktopLauncher` as main class
1. Set `[projectPath]\BrainySnake\core\assets` as working directory
1. Set `brainysnake.desktop.main` as classpath of module
1. Run

## Build Project for Eclipse
1. Checkout project
1. Import the Project with _File>Import>Gradle>Existing Gradle Project_

## Create Run Configuration in Eclipse
1. Create a Run Configuration and choose _Java Application_
1. Set `brainySnake-desktop` as project
1. Set `de.adesso.brainysnake.desktop.DesktopLauncher` as main class
1. Save the new configuration
1. Run

# Rules of the game

![explainSnakePic](/Dokumentation/explainSnake.png)

* Each round of the game the snake can decide to go either right, left, up or down.
* A snake consists of a body and a head.
* If a snake collides with a level element - an obstacle or a level border - it loses one "part" of its body. If a snake loses all its body parts (i.e. if only the head remains), it dies.
* If a snake collects a point, a "new part" will be added to its body (i.e. it gets one unit longer). A point is collected by navigating the snake's head over it.
* A snake can bite another snake. In this case the snake gains a point and enters the "Ghostmode"
    * During Ghostmode a snake is protected from other snake' bites.
    * During Ghostmode a snake can not collect any points or bite other players.
    * The Ghostmode lasts for 10 seconds (depends on config)
* A snake has a specific field of view. The field of view is a two dimensional grid which can contain the following information:
    * Position of level elements (borders, obstacles)
    * Points
    * Other snakes
* If a snake receives an invalid command (e.g. going directly upwards while moving downwards) it switches into the "ConfusedMode". When entering ConfusedMode, the snake loses one point, starts blinking and will not move.


# How to create your own AI?

Implement your snake behavior in the `YourPlayer` class.
You can see the `SamplePlayer` class to get an idea about how to start.
Give your class a unique name - see the `getPlayerName` method to change your snake's name.
To get used to the movements of the snakes you can use the arrow keys on your keyboard to control a special snake which is generated  when the game is loaded.

## Want to contribute?
See the [contribution guide](https://github.com/adessoAG/BrainySnake/blob/master/CONTRIBUTING.md).
