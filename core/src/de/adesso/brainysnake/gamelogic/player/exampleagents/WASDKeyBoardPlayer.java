package de.adesso.brainysnake.gamelogic.player.exampleagents;

import de.adesso.brainysnake.gamelogic.io.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

/**
 * Agent whicht can be controlled by keyboard input (w, a, s, d keys)
 */
public class WASDKeyBoardPlayer implements BrainySnakePlayer {

    private Orientation lastOrientation;

    @Override
    public String getPlayerName() {
        return "WASDKeyBoardPlayer";
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {

        if (KeyBoardControl.A && !Orientation.RIGHT.equals(lastOrientation)) {
            lastOrientation = Orientation.LEFT;
        }
        if (KeyBoardControl.D && !Orientation.LEFT.equals(lastOrientation)) {
            lastOrientation = Orientation.RIGHT;
        }
        if (KeyBoardControl.W && !Orientation.DOWN.equals(lastOrientation)) {
            lastOrientation = Orientation.UP;
        }
        if (KeyBoardControl.S && !Orientation.UP.equals(lastOrientation)) {
            lastOrientation = Orientation.DOWN;
        }

        return new PlayerUpdate(lastOrientation);
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        /* The SamplePlayer is very lazy, it just stores the last data */
        return true;
    }
}
