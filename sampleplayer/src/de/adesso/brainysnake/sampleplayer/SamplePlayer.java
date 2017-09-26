package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

public class SamplePlayer implements BrainySnakePlayer {

    private PlayerState playerState;

    @Override
    public String getPlayerName() {
        return "SamplePlayer";
    }

    @Override
    public void handlePlayerStatusUpdate(PlayerState playerState) {
        /* The SamplePlayer is very lazy, it just stores the last data*/
        this.playerState = playerState;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        return new PlayerUpdate();
    }
}
