Buildstate master: [![CircleCI](https://circleci.com/gh/adessoAG/BrainySnake.svg?style=svg)](https://circleci.com/gh/adessoAG/BrainySnake)

# BrainySnake
![brainySnakePic](/Dokumentation/brainySnake.png)

BrainySnake is a gameController based on the all time classic gameController "Snake". In BrainySnake you don't actively control your snake but instead program its behaviour so it follows certain patterns.
During a gameController, multiple autonomous Snakes are loaded simultaneously and start battling for victory! After 1000 moves (number is adjustable), whoever is still alive and has the most points, wins.

# Open Project

## Build Project for IntelliJ
1. checkout project
1. run **_gradlew idea_** or for reinit **_gradlew cleanidea idea_** in your favorite console
(On Unix **./gradlew cleanidea idea**)
1. open the generated **.ipr** file or **gradlew desktop:run** (On Unix **./gradlew run**)

## Create Desktop Run-Configuration in IntelliJ
1. create a new Application Configuration
1. set _de.adesso.brainysnake.desktop.DesktopLauncher_ as Main class
1. set _[projectPath]\BrainySnake\core\assets_ as Working directory
1. set _desktop_ as classpath of module
1. run

## Build Project for eclipse
1. checkout project
1. import the Project with _File>Import>Gradle>Existing Gradle Project_

## Create Desktop Run-Configuration in eclipse
1. create a Run Configuration and choose _Java Application_
1. Set _brainySnake-desktop_ as projecct
1. Set _de.adesso.brainysnake.desktop.DesktopLauncher_ as Main class
1. Save the new configuration
1. run

## Rules of the gameController

![explainSnakePic](/Dokumentation/explainSnake.jpg)


* Each move the Snake can decide to go either forwards, left or right.
* A Snake consists of a body and a head.
* If a Snake collides with a level element - an obstacle or a level border - it loses one "part" of its body. If a snake loses all its body parts (i.e. if only the head remains) it has lost the gameController and is removed.
* If a Snake collects a point a "part" is added to its body (i.e. it gets one field longer). A point is collected by navigating the Snake's head over it.
* A Snake can bite another snake. In this case the Snake gains a point and enters the "Ghostmode"
    * During Ghostmode a Snake is protected from other Snakes' bites.
    * During Ghostmode a Snake can not collect any points or bite other players.
* A Snake has a specific field of view. The field of view is a two dimensional grid which can contain the following information:
    * Position of level elements (borders, obstacles)
    * Points
    * Other Snakes
* If a Snake receives an invalid command (e.g. going directly upwards while moving downwards) it switches into the "ConfusedMode". When entering ConfusedMode the Snake loses one point and starts blinking.


## How To

Implement your Snake behaviour in the "YourPlayer" class. You can see the "SamplePlayer" class to get an idea about how to start.
Give your class a unique name - see the "getPlayerName" method to change your Snake's name. To get used to the movements of the Snakes you can use the arrow keys on your keyboard to control a special Snake which is generated 
when the gameController is loaded.

## Want to contribute?
See the [contribution guide](https://github.com/adessoAG/BrainySnake/blob/master/CONTRIBUTING.md).
Active Contributors can also join our [BrainySnake Slack](https://brainysnake.slack.com) Channel.
