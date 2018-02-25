package de.adesso.brainysnake.gamelogic.player;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.gamelogic.PlayerBoard;
import de.adesso.brainysnake.gamelogic.level.GlobalGameState;
import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Technical object of a player as Snake.
 * Holds meta-information for rendering the Snake object
 */
public class PlayerHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerHandler.class.getName());

    private PlayerBoard playerBoard;

    private Orientation currentOrientation;

    private List<RoundEvent> roundEvents = new ArrayList<>();

    private PlayerView playerView;

    private PlayerState lastPlayerState;

    private boolean isDead, isGhostMode, isConfused, isBlinking;

    private int ghostTime = 0, blinkTime = 0;

    private Snake snake;

    /**
     * leave nothing to chance
     * This id will identify the player and add its processing
     */
    private UUID playerIdentifier;

    public PlayerHandler(PlayerBoard playerBoard, Snake snake) {
        this.playerBoard = playerBoard;
        this.currentOrientation = snake.getStartOrientation();
        this.snake = snake;
        this.playerIdentifier = UUID.randomUUID();
    }

    public void update() {
        if (isDead) {
            return;
        }

        if (isGhostMode) {
            ghostMode();
        } else if (isConfused) {
            blink();
        } else {
            snake.reset();
        }
    }

    private void ghostMode() {
        if (ghostTime++ > Config.GHOST_TIME) {
            ghostTime = 0;
            snake.reset();
            isGhostMode = false;
        } else {
            snake.setGhostMode();
        }
    }

    private void blink() {
        if (blinkTime++ > Config.BLINK_TIME) {
            blinkTime = 0;
            isBlinking = !isBlinking;
        }

        if (isBlinking) {
            snake.reset();
        } else {
            snake.blink();
        }
    }

    /**
     * Calculates the next PlayerState from global GameData
     */
    public void calculatePlayerState() {

        int points = snake.countPoints();
        Point2D head = snake.getHeadPosition();
        Point2D tail;
        if (points > 1) {
            tail = snake.getTailPosition();
        } else {
            tail = null;
        }
        int ghostModeRemaining = Config.GHOST_TIME - ghostTime;

        boolean bitByPlayer = false;
        boolean moved = false;
        boolean collisionWithLevel = false;

        for (RoundEvent roundEvent : roundEvents) {
            switch (roundEvent) {
                case BIT_BY_PLAYER:
                    bitByPlayer = true;
                    break;
                case MOVED:
                    moved = true;
                    break;
                case COLLISION_WITH_LEVEL:
                    collisionWithLevel = true;
                    break;
            }
        }

        this.lastPlayerState = new PlayerState(GlobalGameState.getPastRounds(), GlobalGameState.movesRemaining(), points, head, tail, isGhostMode, ghostModeRemaining, bitByPlayer, moved, collisionWithLevel, playerView);
    }

    /**
     * Sends the last PlayerState to the Agent Warning: Call this Method from Thread or Test only
     *
     * @return if processing was successful. (This can be ignored)
     */
    Boolean sendPlayerState() {
        return playerBoard.getBrainySnakePlayer().handlePlayerStatusUpdate(this.lastPlayerState);
    }

    /**
     * Requests the next Update (step) from the Agent (player) Warning: Call this Method from Thread or Test only
     *
     * @return PlayerUpdate (this can be null
     */
    PlayerUpdate requestPlayerUpdate() {
        return playerBoard.getBrainySnakePlayer().tellPlayerUpdate();
    }

    public boolean isDead() {
        return isDead;
    }

    public void penalty() {
        if (snake.countPoints() <= 1) {
            kill();
            LOGGER.info("RIP player {}", playerBoard.getName());
        } else {
            snake.removeTail();
        }
    }

    public void updatePlayerBoard() {
        playerBoard.setColor(snake.getHeadColor());
        playerBoard.setDead(isDead);
        playerBoard.setPoints(snake.countPoints());
    }

    public Point2D getNextPositionBy(Orientation orientation) {
        return nextPositionIs(orientation);
    }

    public void moveToNextPosition() {
        this.snake.setNextPosition(nextPositionIs(currentOrientation));
        isConfused = false;
    }

    private Point2D nextPositionIs(Orientation orientation) {
        Point2D nextPosition = snake.getHeadPosition().cpy();
        switch (orientation) {
            case UP:
                nextPosition.add(0, 1);
                break;
            case RIGHT:
                nextPosition.add(1, 0);
                break;
            case DOWN:
                nextPosition.add(0, -1);
                break;
            case LEFT:
                nextPosition.add(-1, 0);
                break;
            default:
        }

        return nextPosition;
    }

    public List<RoundEvent> getRoundEvents() {
        return roundEvents;
    }

    public Snake getSnake() {
        return snake;
    }

    public Point2D getHeadPosition() {
        return snake.getHeadPosition();
    }

    public void setConfused(boolean confused) {
        this.isConfused = confused;
    }

    public void setGhostMode() {
        isGhostMode = true;
        ghostTime = 0;
    }

    public boolean isGhostMode() {
        return isGhostMode;
    }

    public boolean gotBitten(Point2D nextPosition) {
        return snake.containsPosition(nextPosition);
    }

    public void endRound() {
        roundEvents.clear();
    }

    public boolean isOrientationValid(Orientation orientation) {
        switch (orientation) {
            case UP:
                return !currentOrientation.equals(Orientation.DOWN);
            case DOWN:
                return !currentOrientation.equals(Orientation.UP);
            case LEFT:
                return !currentOrientation.equals(Orientation.RIGHT);
            case RIGHT:
                return !currentOrientation.equals(Orientation.LEFT);
        }
        return true;
    }

    public Orientation getCurrentOrientation() {
        return currentOrientation;
    }

    public void setCurrentOrientation(Orientation currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public void updatePlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public UUID getPlayerIdentifier() {
        return playerIdentifier;
    }

    public void kill() {
        isDead = true;
        snake.clear();
    }
}
