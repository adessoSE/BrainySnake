package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.*;

/**
 * Implementiere hier deine Schlangensteuerung.
 */
public class YourPlayer implements BrainySnakePlayer {

    private PlayerState playerState;

    @Override
    public String getPlayerName() {
        return "YourName";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        this.playerState = playerState;
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        return new PlayerUpdate(null);
    }
}
