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

    private PlayerState lastPlayerState;

    private boolean dead, ghostMode, confused, blinked;

    private int ghostTime = 0, blinkTime = 0;

    private Snake snake;

    private String playerName;

    public PlayerHandler(BrainySnakePlayer brainySnakePlayer, Orientation orientation, Snake snake) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.currentOrientation = orientation;
        this.snake = snake;
        this.playerName = (brainySnakePlayer.getPlayerName() == null || brainySnakePlayer.getPlayerName().isEmpty()) ? UUID.randomUUID().toString()
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
        Point2D head = new Point2D(snake.getHeadPosition());
        Point2D tail = new Point2D(snake.getHeadPosition());
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

        List<Field> fieldList = new ArrayList<Field>();
        for (int x = -3; x <= 3; x++) {
            for (int y = 1; y < 6; y++) {
                fieldList.add(new Field(new Point2D(head.x + x, head.y + y), GameObjectType.EMPTY));
            }
        }

        PlayerView playerView = new PlayerView(fieldList);
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
        return this.brainySnakePlayer.tellPlayerUpdate();
    }

    public void kill() {
        dead = true;
        Gdx.app.log("AGENT: ", "Player " + brainySnakePlayer.getPlayerName() + " has dieded");
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

    public void endround() {
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

    public void grantPoint() {

    }
}
