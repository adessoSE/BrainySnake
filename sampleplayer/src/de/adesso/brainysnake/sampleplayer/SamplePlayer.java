package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.*;


/**
 * Beispielsimplementierung eines Spielers.
 */
public class SamplePlayer implements BrainySnakePlayer {

    private PlayerState playerState;
    private PlayerView playerView;

    @Override
    public String getPlayerName() {
        return "SamplePlayer";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        // Verarbeitung des neuen PlayerStatusUpdates. Speicherung des PlayerStates, sowie der beinhalteten PlayerView.
        this.playerState = playerState;
        this.playerView = playerState.getPlayerView();

        // Rückgabewert "true" bestätigt den kompletten Ablauf der Methode.
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        // Speicherung der vorhandenen aktuellen Richtung der Schlange. Definition der neuen Schlangenrichtung, falls nichts bearbeitet wird soll die gleiche Richtung beibehalten werden.
        Orientation currentOrientation = this.playerView.getCurrentOrientation();
        Orientation nextStep = currentOrientation;

        if (isLevelAhead()){
            // Die Schlange weicht nach Links aus, falls die Schlange ein Levelobjekt ein Feld vor sich hat.
            nextStep = turnLeft(currentOrientation);
        }
        else if(isPointVisible()){
            // Definition, falls die Schlange einen Punkt in dem Sichtfeld hat.
        }
        else if (isSnakeAhead()){
            // Definition, falls die Schlange eine andere Schlange ein Feld vor sich hat. ACHTUNG: Kann auch die eigene Schlange sein!
        }

        // Rückgabe des erstellten PlayerUpdate mit der neuen definierten Richtung.
        return new PlayerUpdate(nextStep);
    }

    // Prüfung des Feldes vor der Schlange, ob sich hier eine Levelobjekt befindet.
    private boolean isLevelAhead(){
        // Das Feld 22 spiegelt die Position vor dem Schlangenkopf wieder. Siehe Dokumentation.
        return this.playerView.getVisibleFields().get(22).getFieldType().equals(FieldType.LEVEL);
    }

    // Prüfung des Feldes vor der Schlange, ob sich hier eine Schlange befindet. ACHTUNG: Kann auch die eigene Schlange sein!
    private boolean isSnakeAhead(){
        // Das Feld 22 spiegelt die Position vor dem Schlangenkopf wieder. Siehe Dokumentation.
        return this.playerView.getVisibleFields().get(22).getFieldType().equals(FieldType.PLAYER);
    }

    // Prüfung der sichtbaren Felder, ob sich hier ein Punkt drin befindet.
    private boolean isPointVisible(){
        boolean pointDetected = false;

        for (Field visibleField : this.playerView.getVisibleFields()){
            pointDetected = visibleField.getFieldType().equals(FieldType.POINT);
            if (pointDetected) break;
        }
        return pointDetected;
    }

    // Berechnung der neuen Richtung entsprechend der gegebenen Richtung
    private Orientation turnLeft(Orientation currentOrientation) {
        switch (currentOrientation) {
            case UP:
                return Orientation.LEFT;
            case DOWN:
                return Orientation.RIGHT;
            case LEFT:
                return Orientation.DOWN;
            case RIGHT:
                return Orientation.UP;
        }

        return null;
    }
}