package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;


/**
 * TODO: Implement your agent here
 */
public class YourPlayer implements BrainySnakePlayer {

    private PlayerState playerState;

    @Override
    public String getPlayerName() {
        return "TODO YourName";
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
