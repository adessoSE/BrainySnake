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
    private boolean ghostModeActive;
    private final int ghostModeRemaining;
    private final boolean bitByPlayer;
    private final boolean moved;
    private final boolean collisionWithLevel;
    private final PlayerView playerView;

    public PlayerState(int movesPlayed, int movesRermaining, int playerPoints, Point2D playersHead, Point2D playerTail, boolean ghostModeActive, int ghostModeRemaining, boolean bitByPlayer, boolean moved, boolean collisionWithLevel, PlayerView playerView) {
        this.movesPlayed = movesPlayed;
        this.movesRermaining = movesRermaining;
        this.playerPoints = playerPoints;
        this.playersHead = playersHead;
        this.getPlayersTail = playerTail;
        this.ghostModeActive = ghostModeActive;
        this.ghostModeRemaining = ghostModeRemaining;
        this.bitByPlayer = bitByPlayer;
        this.moved = moved;
        this.collisionWithLevel = collisionWithLevel;
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

    public boolean isGhostModeActive() {
        return ghostModeActive;
    }

    public int getGhostModeRemaining() {
        return ghostModeRemaining;
    }

    public boolean isBitByPlayer() {
        return bitByPlayer;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isCollisionWithLevel() {
        return collisionWithLevel;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }
}