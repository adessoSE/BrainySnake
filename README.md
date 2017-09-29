# BrainySnake
![GitHub Logo](/Dokumentation/brainySnake.png)

## Build Project for IntelliJ
1. checkout project
1. run _gradlew idea_ or for reinit _gradlew cleanidea idea_ in your favorite console
1. open the generated .ipr file

## Create Desktop Run-Configuration in IntelliJ
1. create a knew Application Configuration
1. set _de.adesso.brainysnake.desktop.DesktopLauncher_ as Main class
1. set _[projectPath]\BrainySnake\core\assets_ as Working directory
1. set _desktop_ as classpath of module
1. run

## Spielregeln

![GitHub Logo](/Dokumentation/explainSnake.png)

* Pro Runde kann eine Schlange sich ein Feld nach links, rechts oder nach vorne bewegen.
* Eine Schlange besteht aus Körper und Kopf. 
* Wenn die Schlange mit einem Levelelement kollidiert, verliert sie ein Feld ihres Körpers. Wenn eine Schlange keine Körperfelder übrig hat, stirbt sie und scheidet aus der Spielrunde aus.
* Wenn die Schlange einen Punkt findet, verlängert sich ihr Körper um ein Feld.
* Eine Schlange kann eine andere Schlange beißen. In diesem Fall erhält die Schlange einen Punkt und geht in den Ghostmode
    * Im Ghostmode ist die Schlange vor bissen anderer Schlangen geschützt. 
    * Im Ghostmode kann die Schlange keine Punkte konsumieren.
* Eine Schlange besitzt ein Sichtfeld. Das Sichtfelst ist ein zweidimensionales Raster, über welchen die Schlange folgende Informationen abfragen bzw. sehen kann:
    * Levelelemente(Wände, Hindernisse)
    * Punkt
    * Andere Schlangen
* Wenn eine Schlange ein invalide Steueranweisung erhält (bspw. ist eine 180* Wendung nicht möglich) wechselt die Schlange in den _ConfusedMode_. In diesem Modus erhält die Schlange einen Punkt Abzug und fängt an zu Blinken.
    
## Howto
Implementiere deine Schlangensteuerung in der Klasse _YourPlayer_. Dazu kannst du dich an den Beispielimplementierungen der Klassen _SamplePlayer_ und _SamplePlayer2_ orientieren.
Um mit der Schlangensteuerung vertraut zu werden, wird initial bei Spielstart eine Schlange erzeugt, die über die Pfeiltasten der Tastatur steuerbar ist.

