package de.adesso.brainysnake.sampleplayer;

import java.util.Random;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

public class SamplePlayer implements BrainySnakePlayer {

    private PlayerState playerState;

    @Override
    public String getPlayerName() {
        return "SamplePlayer";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        /* The SamplePlayer is very lazy, it just stores the last data*/
        this.playerState = playerState;
        //System.out.println("Player: " + this.getPlayerName() + " got update");
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        Orientation orientation = Orientation.values()[(new Random().nextInt(Orientation.values().length))];
        return new PlayerUpdate(orientation);
    }
}
