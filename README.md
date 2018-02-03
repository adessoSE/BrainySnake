Buildstate master [![CircleCI](https://circleci.com/gh/adessoAG/BrainySnake.svg?style=svg)](https://circleci.com/gh/adessoAG/BrainySnake)

# BrainySnake
![brainySnakePic](/Dokumentation/brainySnake.png)

Brainysnake ist ein Snake-Spiel bei welchem ihr eure Schlange nicht aktiv selbst steuert, sondern über einen Bewegungsalgorithmus vorher festlegt, wie eure Schlange sich verhalten soll.
Dabei werden pro Spiel mehrere autonome Schlangen in einem Level geladen. Dann beginnt der Kampf um den Sieg. Gewonnen hat, wer nach 1000 Zügen (Anzahl der Züge ist konfigurierbar) noch lebt und die meisten Punkte hat.

# Open Project

## Build Project for IntelliJ
1. checkout project
1. run **_gradlew idea_** or for reinit **_gradlew cleanidea idea_** in your favorite console
(On Unix **:/gradlew cleanidea idea**)
1. open the generated **.ipr** file or **gradlew desktop:run**

## Create Desktop Run-Configuration in IntelliJ
1. create a new Application Configuration
1. set _de.adesso.brainysnake.desktop.DesktopLauncher_ as Main class
1. optional: set _[projectPath]\BrainySnake\core\assets_ as Working directory
1. optional: set _desktop_ as classpath of module
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

## Spielregeln

![explainSnakePic](/Dokumentation/explainSnake.jpg)

* Pro Runde kann die Schlange sich um ein Feld nach links, rechts oder nach vorne bewegen.
* Eine Schlange besteht aus Körper und Kopf.
* Wenn die Schlange mit einem Levelelement kollidiert, verliert sie ein Feld ihres Körpers. Wenn eine Schlange keine Körperelemente übrig hat, stirbt sie und  scheidet aus der Spielrunde aus.
* Wenn die Schlange einen Punkt einsammelt, verlängert sich ihr Körper um ein Feld. Ein Punkt wird eingesammelt indem die Schlange mit ihrem Kopf über den Punkt steuert.
* Eine Schlange kann eine andere Schlange beißen. In diesem Fall erhält die Schlange einen Punkt und geht in den _Ghostmode_
    * Im Ghostmode ist die Schlange vor Bissen anderer Schlangen geschützt
    * Im Ghostmode kann die Schlange keine Punkte konsumieren
* Eine Schlange besitzt ein Sichtfeld. Das Sichtfeld ist ein zweidimensionales Raster, über welchen die Schlange folgende Informationen abfragen bzw. sehen kann:
    * Levelelemente (Wände, Hindernisse)
    * Punkte
    * Andere Schlangen
* Wenn eine Schlange ein invalide Steueranweisung erhält (bspw. ist eine 180* Wendung nicht möglich) wechselt die Schlange in den _ConfusedMode_. In diesem Modus erhält die Schlange einen Punkt Abzug und fängt an zu blinkern.


## Howto
Implementiere deine Schlangensteuerung in der Klasse _YourPlayer_. Dazu kannst du dich an den Beispielimplementierungen der Klassen _SamplePlayer_ und _SamplePlayer2_ orientieren.
Gib deiner Klasse einen eindeutigen Namen (siehe dazu die Methode **getPlayerName** in der YourPlayer Klasse)
Um mit der Schlangensteuerung vertraut zu werden, wird initial bei Spielstart eine Schlange erzeugt, welche über die Pfeiltasten der Tastatur steuerbar ist.

## Want to contribute?
See the [contribution guide](https://github.com/adessoAG/BrainySnake/blob/master/CONTRIBUTING.md).
Active Contributors can also join our [BrainySnake Slack](https://brainysnake.slack.com) Channel.
