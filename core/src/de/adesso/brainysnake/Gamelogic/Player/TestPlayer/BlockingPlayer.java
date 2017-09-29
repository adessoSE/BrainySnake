package de.adesso.brainysnake.Gamelogic.Player.TestPlayer;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

import static de.adesso.brainysnake.playercommon.Orientation.RIGHT;

public class BlockingPlayer implements BrainySnakePlayer {
    @Override
    public String getPlayerName() {
        return "BlockingPlayer";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        int i = 1;
        while (i == 1) {
            i = 1;
        }
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        int i = 1;
        while (i == 1) {
            i = 1;
        }
        return new PlayerUpdate(RIGHT);
    }
}
