package de.adesso.brainysnake.gamelogic.level;


public class GlobalGameState {

    private static int roundsToPlay = 0;

    private static int pastRounds = 0;

    public static void initialize(int roundsToPlay) {
        GlobalGameState.roundsToPlay = roundsToPlay;
        GlobalGameState.pastRounds = 0;
    }

    public static int movesRemaining() {
        return roundsToPlay - pastRounds;
    }

    public static int getPastRounds() {
        return pastRounds;
    }

    /**
     * Increases the number of rounds by one and returns the current round
     *
     * @return current Round
     */
    public static int increasePastRounds() {
        return pastRounds++;
    }
}
