# Dokumentation des Simulationsspiels BrainySnake

- [Dokumentation des Simulationsspiels BrainySnake](#dokumentation-des-simulationsspiels-brainysnake)
    - [Spiel aus Sicht des Players](#spiel-aus-sicht-des-players)
    - [Herausforderungen für die Entwickler der Agenten](#herausforderungen-f%C3%BCr-die-entwickler-der-agenten)
    - [Technische Umsetzung](#technische-umsetzung)
        - [Objekt: _PlayerState_](#objekt-playerstate)
        - [Objekt: _PlayerView_](#objekt-playerview)
        - [Objekt: _RoundEvent_](#objekt-roundevent)
    - [Projektstruktur (Gradle)](#projektstruktur-gradle)
    - [Programmablauf](#programmablauf)
        - [Simulationsablauf](#simulationsablauf)
    - [Technische Herausforderungen](#technische-herausforderungen)
        - [Der Agent antwortet nicht](#der-agent-antwortet-nicht)
        - [Der Agent greift auf Fremden Code zu](#der-agent-greift-auf-fremden-code-zu)
    - [Glossar](#glossar)


## Spiel aus Sicht des Players

Jeder Spieler ist ein **Agent**, der alle Entscheidungen über seine Züge selbst trifft.
Dazu wird der Agent von der Simulation über den neuen Zustand informiert.
Der Agent wird von der Simulation nach seiner nächsten Aktion gefragt.

## Herausforderungen für die Entwickler der Agenten

1. **Herausforderungen Stufe #1**
    1. Der Agent sollte seinen Status (PlayerStatus) von der Simulation entgegennehmen können und verstehen wo das Kopfelement ist und entsprechend handeln. Befindet sich das Kopfelement an der Position (0, 0) muss Agent
    wissen, dass es nur eine gültige Zugrichtung gibt. Ebenso kann der Agent aus der Sicht nach vorne (PlayerStatus -> PlayerView) erkennen, dass die Elemente links und vorne Wandelemente sind (WallElement)
    2. Der Angent kann seine Positionen selbst verwalten und liefert auf Anfrage der Simulation einen gültigen Zug.
2. **Herausforderungen Stufe #2**
    1. Der Agent kann sich selbständig und strategisch in der Spielwelt bewegen und verfolgt eine Strategie.
    Beispiel: Der Agent hält sich von gegnerischen Einheiten fern und beginnt frühzeitig mit dem Wachstum
3. **Herausforderungen der Stufe #3**
    1. Der Agent ist in der Lage das Verhalten der Gegner einzubeziehen.
4. **Herausforderungen Stufe #4**
    1. Der Agent ist selbstlernend. Daher wird das Verhalten durch die Entwickler nicht mehr explizit vorgegeben.

## Technische Umsetzung

Jeder Agent implementiert das Interface **BrainySnakePlayer**, welches folgende Methoden vorgibt:
- **String getPlayerName()**
-- Agent wird nach dem gesetztem Spielernamen gefragt

- **boolean handlePlayerStatusUpdate (PlayerState playerState)**
-- Der Agent bekommt ein Update vom Spiel und kann damit seinen Datenstand aktualisieren.

- **PlayerUpdate tellPlayerUpdate ()**
-- Agent wird nach seinem nächsten Spielzug gefragt


### Objekt: PlayerState

Der Agent erhält das Objekt PlayerState in seiner Methode *handlePlayerStatusUpdate()* übergeben. In dem Objekt kann der Zustand der eigenen Schlange abgefragt werden.

Übersicht der vorhandenen Daten:

|    Name / Methode            |Beschreibung|Datentyp|
|:----------------|:-------------------------------|:-----------------------------:|
|movesPlayed / getMovesPlayed()| Anzahl der gespielten Runden.|int
|movesRemaining / getMovesRemaining()|Anzahl der übrigen zu spielenden Runden.|int
|playerPoints / getPlayerPoints()|Aktuelle Spielerpunkte (Gesamtzahl von Kopf und Körperteileanzahl).|int
|playersHead / getPlayersHead()|Aktuelle Position des Kopfes von der Schlange.|Point2D
|playersTail / getPlayersTail()|Aktuelle Position des Körpers von der Schlange. Es wird nur der letzte Punkt des Körpers zurückgegeben. Falls kein Körper vorhanden wird null zurückgegeben.|Point2D
|ghostModeActive / isGhostModeActive()|Zustand des GhostMode bei der Schlange (GhostMode aktiv = true, GhostMode inaktiv = false).|boolean
|ghostModeRemaining / getGhostModeRemaining()|Verbleibende Dauer von dem aktiven GhostMode. Bei nicht aktivem GhostMode wir die definierte maximale Dauer zurückgegeben.|int
|bitByPlayer / isBitByPlayer()|Mit dieser Methode kann der Zustand über einen Biss an einem Spieler geprüft werden.|boolean
|moved / isMoved()|Mit dieser Methode kann der Zustand über die Bewegung der Schlage des Spielers geprüft werden.|boolean
|collisionWithLevel / isCollisionWithLevel()|Mit dieser Methode kann der Zustand über die Kollision der Schlage des Spielers mit dem Level geprüft werden.|boolean
|playerView / getPlayerView()|Die aktuelle Sicht der Schlange in dem Spielzug. Beinhaltet das Objekt PlayerView.|PlayerView


### Objekt: PlayerView

Der Agent erhält ein zweidimensionales Sichtfeld(viewWidth und viewRange), welches Koordinaten und Bezeichnung von allen Gegenständen enthält. Das Sichtfeld beginnt auf höhe des Kopfes (Kopfposition plus einen Schritt nach vorne). Jedes Field innerhalb des PlayerView hat eine Position und kann ein GameObject enthalten.

Übersicht der vorhandenen Daten:

|    Name / Methode             |Beschreibung                       |Datentyp                       |
|----------------               |-------------------------------    |:-----------------------------:|
|currentOrientation/ getCurrentOrientation()| Aktuelle Orientierung der Schlange.|Orientation
|viewWidth / getViewWidth()|Feldbreite in Feldern aus Sicht der Schlange. Das Feld beginnt vor dem Kopf der Schlange. Die Aufteilung findet in gleichen Anteilen nach rechts & links der Schlange statt.|int
|viewRange / getViewRange()|Feldhöhe in Feldern aus Sicht der Schlange. Beginnend einen Punkt vor dem Kopf der Schlange.|int
|visibleFields / getVisibleFields()|Liste mit allen sichtbaren Feldern für die Schlange. Der erste Eintrag in der Liste mit dem Index 0 ist aus Sicht der Schlange der Punkt links oben.|List of Fields

### Objekt: RoundEvent

Jede Runde werden für die jeweiligen Schlangen verschiedene RoundEvents berechnet. Diese RoundEvents können in dem PlayerState ausgelesen werden.

Übersicht der vorhandenen Events:

|    Name                       |Beschreibung                       |Abfragbar (siehe PlayerState)
|----------------               |-------------------------------    |:-----------------:
|MOVED                          | Bewegung der Schlange.                                    | true
|BIT_BY_PLAYER                  | Biss an der eigenen Schlange von einem anderen Spieler.   | true
|COLLISION_WITH_LEVEL           | Kollision mit Levelobjekten.                              | true
|CONFUSED                       | Verwirrung der Schlange durch ungültiges Update.          | false
|BIT_AGENT                      | Biss von der eigenen Schlange an einem anderen Spieler.   | false
|BIT_HIMSELF                    | Biss von der eigenen Schlange an sich selber.             | false
|DIED                           | Löschung der Schlange aufgrund Punktestand.               | false
|CONSUMED_POINT                 | Konsum von Spielpunkten.                                  | false


## Projektstruktur (Gradle)

Jeder Player (Agent) wird in ein **seperates Modul** ausgelagert, welches in den Core improtiert wird.  
Diese Auslagerung verhindert den Zugriff von den Agent auf fremden Code.
Jedes Player Modul implementiert das Interface **BrainySnakePlayer** und importiert das Package **playerCommon**, welches das Interface und die Klassen beinhaltet (z.B. PlayerStatus, GameEvent und das BrainySnake Interface).

Modul           | Beschreibung                                          | Includes
---------       | ------------------                                    |:--------:
desktop         | Starter für die Anwendung                             | core
core            | Logik der Simulation                                  | SamplePlayer, PlayerOne, PlayerTwo, ...
playerCommon    | Klassen, Interfaces, Enums die alle Agenten brauchen  | junit
samplePlayer    | Beispielimplementierung                               | playerCommon
playerOne       | Spieler (Agent) der zu Implementieren ist             | playerCommon


## Programmablauf

### Simulationsablauf

1. Spiel Initialisierung
1. Start Gameloop
   1. Prüfen ob Zeit bzw. Züge abgelaufen
   1. Alle Spieler erhalten den aktuellen Playerstate
   1. Aktionen aller Agenten einsammeln
   1. Validät der Aktionen prüfen(ungültige Aktionen werde nicht weiter behandelt und die Reaktion darauf in das Playerupdate gespeichert)
   1. Ausführung der Playeractions
   1. Punkte verteilen(Punkt addieren(max ein Punkt), Punkt abziehen)
   1. Todesprüfung
   1. Punktetafel aktualisieren
   1. Draw()
1. Anzeige Endstatus

## Technische Herausforderungen
### Der Agent antwortet nicht

__Problem:__ Fehler, der auftreten kann, wenn der Agent verschuldet oder unverschuldet in einer Endlosschleife läuft.

**Präventiv Lösung:** Unit-Tests schreiben.

**Laufzeit Lösung:** Der Aufruf jedes Agenten erfolgt ein eigenem Thread, welcher nach einer angemessenen Zeit terminiert, sofern keine Antwort eingegangen ist.
Der Agent wird eingefroren (PlayerStatus frozen), wechselt zur Kennzeichnung des Status die Farbe und es werden zu diesem Zug keine Aktionen für diesen Agenten ausgeführt.

### Der Agent greift auf Fremden Code zu
__Problem:__ Der Agent könnte **Public Methoden** der Simulation der oder die **Public Methoden** anderer Agenten zugreifen.
Denkbar sind auch **Reflections** mit der Private Methoden aufgerufen werden.

**Präventiv Lösung:** Wir verbieten das und kontrollieren den Quellcode der Agenten

**Laufzeit Lösung:**

 - Die Simulation verwendet **keine öffentlichen Interfaces**, die Agenten werden von der Simulation aufgerufen.
 - Jeder Agent bekommt ein eigenes Paket
 - **TODO:** Gibt es weitere Möglichkeiten Code zu isolieren?

 ## Glossar
 - Ghostmode: Im Ghostmode kann der Agent keine Aktionen ausfürhen außer sich zu bewegen
