package de.adesso.brainysnake.Gamelogic.Player.TestPlayer;

import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

public class KeyBoardPlayerTwo implements BrainySnakePlayer {

    PlayerState playerState;
    private boolean w, a, s, d;

    @Override
    public String getPlayerName() {
        return "KeyBoardPlayerTwo";
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {

        if (KeyBoardControl.A && !d) {
            a = true;
            d = w = s = false;
        }
        if (KeyBoardControl.D && !a) {
            d = true;
            w = a = s = false;
        }
        if (KeyBoardControl.W && !s) {
            w = true;
            a = s = d = false;
        }
        if (KeyBoardControl.D && !w) {
            d = true;
            w = a = s = false;
        }

        if (a) {
            return new PlayerUpdate(Orientation.LEFT);
        }
        if (d) {
            return new PlayerUpdate(Orientation.RIGHT);
        }
        if (w) {
            return new PlayerUpdate(Orientation.UP);
        }
        if (s) {
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
