package de.adesso.brainysnake.gamelogic.player;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

import java.util.Random;

public class SlowPlayer implements BrainySnakePlayer {

    @Override
    public String getPlayerName() {
        return "SlowPlayer";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Orientation orientation = Orientation.values()[(new Random().nextInt(Orientation.values().length))];
        return new PlayerUpdate(orientation);
    }
}
