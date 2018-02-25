package de.adesso.brainysnake.playercommon;

import de.adesso.brainysnake.playercommon.math.Point2D;

/**
 * The PlayerState is immutable.
 * The BrainySnakePlayer should not change any values passed to the player via update
 */
public final class PlayerState {

    private final int movesPlayed;
    private final int movesRemaining;
    private final int playerPoints;
    private final Point2D playersHead;
    private final Point2D playersTail;
    private boolean ghostModeActive;
    private final int ghostModeRemaining;
    private final boolean bitByPlayer;
    private final boolean moved;
    private final boolean collisionWithLevel;
    private final PlayerView playerView;

    public PlayerState(int movesPlayed, int movesRemaining, int playerPoints, Point2D playersHead, Point2D playerTail, boolean ghostModeActive, int ghostModeRemaining, boolean bitByPlayer, boolean moved, boolean collisionWithLevel, PlayerView playerView) {
        this.movesPlayed = movesPlayed;
        this.movesRemaining = movesRemaining;
        this.playerPoints = playerPoints;
        this.playersHead = playersHead;
        this.playersTail = playerTail;
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

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public int getPlayerPoints() {
        return playerPoints;
    }

    public Point2D getPlayersHead() {
        return playersHead;
    }

    public Point2D getPlayersTail() {
        return playersTail;
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
