package de.adesso.brainysnake.playercommon;

public interface BrainySnakePlayer {

    /**
     * Identifies the player in the game view
     * @return an obvious name
     */
    public String getPlayerName();

    /**
     * Will be called at the beginning of each round to inform the player (agent)
     * about its state within the game, until the game ends or the player dies.
     * @param playerState
     */
    public void handlePlayerStatusUpdate(PlayerState playerState);

    /**
     * Tells the players choice
     * @return
     */
    public PlayerUpdate tellPlayerUpdate();
}
