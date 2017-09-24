# Konzeption des Simulationsspiels BrainySnake

- [Konzeption des Simulationsspiels BrainySnake](#konzeption-des-simulationsspiels-brainysnake)
    - [Spiel aus Sicht des Players](#spiel-aus-sicht-des-players)
    - [Herausforderungen für die Entwickler der Agenten](#herausforderungen-f%C3%BCr-die-entwickler-der-agenten)
    - [Technische Umsetzung](#technische-umsetzung)
        - [Object: _PlayerStatus_](#object-playerstatus)
        - [Object: _PlayerView_](#object-playerview)
    - [Technische Herausforderungen](#technische-herausforderungen)
        - [Der Agent antwortet nicht](#der-agent-antwortet-nicht)
        - [Der Agent greift auf Fremden Code zu](#der-agent-greift-auf-fremden-code-zu)


## Spiel aus Sicht des Players

Jeder Spieler ist ein **Agent**, der alle Entscheidungen über seine Züge selbst trifft.  
Dazu wird der Agent von der Simulation über den neuen Zustand informiert.  
Der Agent wird von der Simulation nach seiner nächsten Aktion gefragt.  

## Herausforderungen für die Entwickler der Agenten

1. Herausforderungen Stufe #1  
    1. Der Agent sollte seinen Status (PlayerStatus) von der Simulation entgegennehmen können und verstehen wo das Kopfelement ist und entsprechend handeln. Befindet sich das Kopfelement an der Position (0, 0) muss Agent 
    wissen, dass es nur eine gültige Zugrichtung gibt. Ebenso kann der Agent aus der Sicht nach vorne (PlayerStatus -> PlayerView) erkennen, dass die Elemente links und vorne Wandelemente sind (WallElement)
    2. Der Angent kann seine Positionen selbst verwalten und liefert auf Anfrage der Simulation einen gültigen Zug.
2. Herausforderungen Stufe #2  
    1. Der Agent kann sich selbständig und strategisch in der Spielwelt bewegen und verfolgt eine Strategie.  
    Beispiel: Der Agent hält sich von gegnerischen Einheiten fern und beginnt frühzeitig mit dem Wachstum
3. Herausforderungen der Stufe #3
    1. Der Agent ist in der Lage das Verhalten der Gegner einzubeziehen.
4. Herausforderungen Stufe #4
    1. Der Agent ist selbstlernend. Daher wird das Verhalten durch die Entwickler nicht mehr explizit vorgegeben. 

## Technische Umsetzung
Jeder Agent implementiert das Interface BrainySnake, welches folgende Methoden vorgibt:
- **void handlePlayerStatusUpdate (PlayerStatus playerStatus)**
    - Der Agent bekommt ein Update vom Spiel und kann damit sein Datenstand aktualisieren.
- **PlayerUpdate getPlayerUpdate()** 
    - Agent wird nach seinem nächsten Spielzug gefragt

### Object: _PlayerStatus_
 - number roundsPlayed     _// Anzahl der gespielten Runden_
 - number roundsRemaining  _// Verbleibende RUnden im Spiel_
 - number PlayerPoints     _// Erspielte Punkte im Spiel (Länge der Schlange)_
 - PlayerPosition head     _// Kopf der Schlange
 - PlayserPosition tail     _// Ende der Schlange_
 - boolen gotPenalty           _// Der Agent hat eine Strafe erhalten_
 - PlayerPenalty penalty     _// Enum Art und Grund der Strafe_
 - PlayerView view          _// Sicht des Spielers auf das Spielfeld_

### Object: _PlayerView_
Der Agent sieht X Felder zur Seite und Y nach vorne
Jedes **Field** hat hat eine **Position** und kann ein **GameObject** enthalten.

## Technische Herausforderungen

### Der Agent antwortet nicht

__Problem:__ Fehler, der auftreten kann, wenn der Agent verschuldet oder unverschuldet in einer Endlosschleife läuft  
**Präventiv Lösung:** Unit-Tests schreiben  
**Laufzeit Lösung:** Der Aufruf jedes Agenten erfolgt ei einem eigenem Thread, welcher nach einer angemessenen Zeit terminiert, sofern keine Antwort eingegangen ist.  
(Der Agent erhält eine Strafe und die Simulation löst die nächste Runde aus)   

### Der Agent greift auf Fremden Code zu
**Problem:** Der Agent könnte **Public Methoden** der Simulation der oder die **Public Methoden** anderer Agenten zugreifen.
Denkbar sind auch **Reflections** mit der Private Methoden aufgerufen werden.  
**Präventiv Lösung:** Wir verbieten das und kontrollieren den Quellcode der Agenten
**Laufzeit Lösung:**  

 1. Die Simulation verwendet **keine öffentlichen Interfaces**, die Agenten werden von der Simulation aufgerufen.  
 1. Jeder Agent bekommt ein eigens Paket
 1. **TODO:** Gibt es weitere Möglichkeiten Code zu isolieren?
