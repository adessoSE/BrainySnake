package de.adesso.brainysnake.Gamelogic.Level;

import de.adesso.brainysnake.Config;

public class GlobalGameState {

    public static int countMoves = 0;

    public static final int maxRounds = Config.MAX_ROUNDS;

    public static int movesRemaining(){
        return maxRounds - countMoves;
    }

}
