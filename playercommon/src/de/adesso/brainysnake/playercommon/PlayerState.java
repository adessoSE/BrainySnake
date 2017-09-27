package de.adesso.brainysnake.playercommon;

import de.adesso.brainysnake.playercommon.math.Point2D;

/**
 * The PlayerState is immutable.
 * The BrainySnakePlayer should not change any values passed to the Player via update
 */
public final class PlayerState {

    private final int movesPlayed;
    private final int movesRermaining;
    private final int playerPoints;
    private final Point2D playersHead;
    private final Point2D getPlayersTail;
    private final boolean gotPenalty;
    private final GameEvent playerGameEvent;
    private final boolean ghostMode;
    private final PlayerView playerView;

    public PlayerState(int movesPlayed, int movesRermaining, int playerPoints, Point2D playersHead, Point2D getPlayersTail, boolean gotPenalty, GameEvent playerGameEvent, boolean ghostMode, PlayerView playerView) {
        this.movesPlayed = movesPlayed;
        this.movesRermaining = movesRermaining;
        this.playerPoints = playerPoints;
        this.playersHead = playersHead;
        this.getPlayersTail = getPlayersTail;
        this.gotPenalty = gotPenalty;
        this.playerGameEvent = playerGameEvent;
        this.ghostMode = ghostMode;
        this.playerView = playerView;
    }

    public int getMovesPlayed() {
        return movesPlayed;
    }

    public int getMovesRermaining() {
        return movesRermaining;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public Point2D getPlayersHead() {
        return playersHead;
    }

    public Point2D getGetPlayersTail() {
        return getPlayersTail;
    }

    public boolean isGotPenalty() {
        return gotPenalty;
    }

    public GameEvent getPlayerGameEvent() {
        return playerGameEvent;
    }

    public boolean isGhostMode() {
        return ghostMode;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }
}
