package de.adesso.brainysnake.gamelogic.player;

import de.adesso.brainysnake.playercommon.Orientation;

public class PlayerChoice {

    private boolean hasChosen;
    private Orientation orientation;

    public PlayerChoice(Orientation orientation) {
        this.orientation = orientation;
        this.hasChosen = true;
    }

    public PlayerChoice() {
        this.hasChosen = false;
    }

    public static PlayerChoice createNoChoice() {
        return new PlayerChoice();
    }

    public boolean isHasChosen() {
        return hasChosen;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
