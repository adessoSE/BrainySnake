# Konzeption des Simulationsspiels BrainySnake

- [Konzeption des Simulationsspiels BrainySnake](#konzeption-des-simulationsspiels-brainysnake)
    - [Spiel aus Sicht des Players](#spiel-aus-sicht-des-players)
    - [Herausforderungen für die Entwickler der Agenten](#herausforderungen-f%C3%BCr-die-entwickler-der-agenten)
    - [Technische Herausforderungen](#technische-herausforderungen)
        - [Der Agent antwortet nicht](#der-agent-antwortet-nicht)
        - [Der Agent greift auf Fremden Code zu](#der-agent-greift-auf-fremden-code-zu)


## Spiel aus Sicht des Players

Jeder Spieler ist ein **Agent**, der alle Entscheidungen über seine Züge selbst trifft.  
Dazu wird der Agent von der Simulation über den neuen Zustand informiert.  
Der Agent wird von der Simulation nach seiner nächsten Aktion gefragt.  

## Herausforderungen für die Entwickler der Agenten

1. Herausforderungen der Stufe #1  
    1.1. Der Agent sollte seinen Status (PlayerStatus) von der Simulation entgegennehmen können und verstehen wo das Kopfelement ist und entsprechend handeln.  
2. Herauforderungen der Stufe #2  



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
