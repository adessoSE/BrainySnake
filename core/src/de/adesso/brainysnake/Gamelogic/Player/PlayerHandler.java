package de.adesso.brainysnake.Gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.math.Point2D;

//TODO Technisch von Logisch trennen
public class PlayerHandler {

    private Orientation currentOrientation;

    private BrainySnakePlayer brainySnakePlayer;

    private List<RoundEvent> roundEvents = new ArrayList<RoundEvent>();

    private PlayerView playerView;

    private PlayerState lastPlayerState;

    private boolean dead, ghostMode, confused, blinked;

    private int ghostTime = 0, blinkTime = 0;

    private Snake snake;

    private String playerName;

    /**
     * leave nothing to chance
     * This id will identify the player and add its processing
     */
    private UUID playerIdentifier;

    public PlayerHandler(BrainySnakePlayer brainySnakePlayer, Orientation orientation, Snake snake) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.currentOrientation = orientation;
        this.snake = snake;
        this.playerIdentifier = UUID.randomUUID();
        this.playerName = (brainySnakePlayer.getPlayerName() == null || brainySnakePlayer.getPlayerName().isEmpty()) ? this.playerIdentifier.toString()
                : this.brainySnakePlayer.getPlayerName().trim();

    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void update() {
        if (dead) {
            return;
        }

        if (ghostMode) {
            ghostMode();
        } else if (confused) {
            blink();
        } else {
            snake.reset();
        }
    }

    private void ghostMode() {
        if (ghostTime++ > Config.GHOST_TIME) {
            ghostTime = 0;
            snake.reset();
            ghostMode = false;
        } else {
            snake.setGhostMode();
        }
    }

    private void blink() {
        if (blinkTime++ > Config.BLINK_TIME) {
            blinkTime = 0;
            blinked = !blinked;
        }

        if (blinked) {
            snake.reset();
        } else {
            snake.blink();
        }
    }

    /**
     * Calculates the next PlayerState from global GameData
     */
    public void calculatePlayerState(GlobalGameState gameState) {

        int points = snake.countPoints();
        Point2D head = snake.getHeadPosition();
        Point2D tail;
        if(points > 1){
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

        this.lastPlayerState = new PlayerState(GlobalGameState.countMoves, GlobalGameState.movesRemaining(), points, head, tail,ghostMode, ghostModeRemaining, bitByPlayer, moved, collisionWithLevel, playerView);
    }

    /**
     * Sends the last PlayerState to the Agent Warning: Call this Method from Thread or Test only
     * 
     * @return if processing was successful. (This can be ignored)
     */
    Boolean sendPlayerState() {
        return this.brainySnakePlayer.handlePlayerStatusUpdate(this.lastPlayerState);
    }

    /**
     * Requests the next Update (step) from the Agent (Player) Warning: Call this Method from Thread or Test only
     * 
     * @return PlayerUpdate (this can be null
     */
    PlayerUpdate requestPlayerUpdate() {
        return brainySnakePlayer.tellPlayerUpdate();
    }

    public void kill() {
        dead = true;
        Gdx.app.log("AGENT: ", "Player " + brainySnakePlayer.getPlayerName() + " has died");
    }

    public boolean isDead() {
        return dead;
    }

    public void penalty() {
        if (snake.countPoints() <= 1) {
            dead = true;
        } else {
            snake.removeTail();
        }
    }

    public Point2D getNextPositionBy(Orientation orientation) {
        return nextPositionIs(orientation);
    }

    public void moveToNextPosition() {
        this.snake.setNextPosition(nextPositionIs(currentOrientation));
        confused = false;
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

    public Point2D getHeadPosition(){
        return snake.getHeadPosition();
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public void setGhostMode() {
        ghostMode = true;
        ghostTime = 0;
    }

    public boolean isGhostMode() {
        return ghostMode;
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

    public void setCurrentOrientation(Orientation currentOrientation) {
        this.currentOrientation = currentOrientation;
    }

    public Orientation getCurrentOrientation() {
        return currentOrientation;
    }

    public void grantPoint() {

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
}
