package de.adesso.brainysnake.Gamelogic.Player.TestPlayer;

import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

public class KeyBoardPlayer implements BrainySnakePlayer {

    PlayerState playerState;
    private boolean left, right, up, down;

    @Override
    public String getPlayerName() {
        return "KeyBoardPlayer";
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {

        System.out.println(playerState.getGhostModeRemaining());

        if (KeyBoardControl.LEFT && !right) {
            left = true;
            right = up = down = false;
        }
        if (KeyBoardControl.RIGHT && !left) {
            right = true;
            left = up = down = false;
        }
        if (KeyBoardControl.UP && !down) {
            up = true;
            left = right = down = false;
        }
        if (KeyBoardControl.DOWN && !up) {
            down = true;
            left = right = up = false;
        }

        if (left) {
            return new PlayerUpdate(Orientation.LEFT);
        }
        if (right) {
            return new PlayerUpdate(Orientation.RIGHT);
        }
        if (up) {
            return new PlayerUpdate(Orientation.UP);
        }
        if (down) {
            return new PlayerUpdate(Orientation.DOWN);
        }

        return null;
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
            /* The SamplePlayer is very lazy, it just stores the last data */
        this.playerState = playerState;
        return true;
    }
}
