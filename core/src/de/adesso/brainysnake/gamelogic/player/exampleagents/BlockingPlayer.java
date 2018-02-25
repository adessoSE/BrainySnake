package de.adesso.brainysnake.gamelogic.player.exampleagents;

import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

import static de.adesso.brainysnake.playercommon.Orientation.RIGHT;

/**
 * Testclass for agents, which takes much time to make a decision.
 * For testing thread handling with blocking agents.
 */
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
