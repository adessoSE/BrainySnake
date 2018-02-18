package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;


/**
 * Implementiere hier deine Schlangensteuerung.
 */
public class YourPlayer implements BrainySnakePlayer {

    private PlayerState playerState;

    public YourPlayer() {
    }

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
