package de.adesso.brainysnake.Gamelogic.Player.TestPlayer;

import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

public class KeyBoardPlayer implements BrainySnakePlayer {

    private Orientation lastOrientation;

    @Override
    public String getPlayerName() {
        return "KeyBoardPlayer";
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {

        if (KeyBoardControl.LEFT && !Orientation.RIGHT.equals(lastOrientation)) {
            lastOrientation = Orientation.LEFT;
        }
        if (KeyBoardControl.RIGHT && !Orientation.LEFT.equals(lastOrientation)) {
            lastOrientation = Orientation.RIGHT;
        }
        if (KeyBoardControl.UP && !Orientation.DOWN.equals(lastOrientation)) {
            lastOrientation = Orientation.UP;
        }
        if (KeyBoardControl.DOWN && !Orientation.UP.equals(lastOrientation)) {
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
