package de.adesso.brainysnake.Gamelogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Utils;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.math.Point2D;

// TODO Kopf-Farbe kenntlich machen
// Technisch von Logisch trennen
public class PlayerHandler {

    private Orientation currentOrientation;

    private BrainySnakePlayer brainySnakePlayer;

    private List<RoundEvents> roundEvents = new ArrayList<RoundEvents>();

    private PlayerState lastPlayerState;

    private boolean dead, ghostMode, confused, blinked;

    private int ghostTime = 0;

    private Orientation orientation;

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

    private void update(float delta) {

        if (dead) {
            return;
        }

        if (ghostMode) {
            ghostMode();
        } else if (confused) {
            blink();
        }
    }

    private void ghostMode() {
        if (ghostTime++ > Config.GHOST_TIME) {
            ghostTime = 0;
            snake.setGhostMode();
            ghostMode = false;
        } else {
            snake.reset();
        }
    }

    private void blink() {
        blinked = !blinked;

        if (blinked) {
            snake.blink();
        } else {
            snake.reset();
        }
    }

    /**
     * Calculates the next PlayerState from global GameData
     */
    public void calculatePlayerState(GlobalGameState gameState) {
        // TODO Calc PlayerState from GlobalGameState

        // TODO hier noch den korrekten playerstate berechnen
        // <Fake Data>
        Point2D head = Utils.fromGridPoint2(new GridPoint2(5, 5));
        Point2D tail = Utils.fromGridPoint2(new GridPoint2(5, 1));

        List<Field> fieldList = new ArrayList<Field>();
        for (int x = -3; x <= 3; x++) {
            for (int y = 1; y < 6; y++) {
                fieldList.add(new Field(new Point2D(head.x + x, head.y + y), GameObjectType.EMPTY));
            }
        }

        PlayerView playerView = new PlayerView(fieldList);
        this.lastPlayerState = new PlayerState(100, 200, 5, head, tail, false, RoundEvents.MOVED, false, playerView);
        // </Fake Data>
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public PlayerState getLastPlayerState() {
        return lastPlayerState;
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

    public void penalty() {
        snake.removeTail();
    }

    private int getOrientationByEnum(Orientation orientation) {
        Orientation[] orientationValues = Orientation.values();
        for (int i = 0; i < orientationValues.length; i++) {
            if (orientation.equals(orientationValues[i])) {
                return i;
            }
        }

        return -1;
    }

    /**
     * What is the next Agent Head Position depending on the next GameEvent
     *
     * @return
     */
    public Point2D getNextPosition() {
        return nextPositionIs(currentOrientation);
    }

    public void moveToNextPosition() {
        this.snake.setNextPosition(nextPositionIs(currentOrientation));
        confused = false;
    }

    private Point2D nextPositionIs(Orientation orientation) {
        Point2D nextPosition = null;
        Point2D cpy = snake.getHeadPosition().cpy();
        switch (orientation) {
            case UP:
                nextPosition = cpy.add(0, 1);
                break;
            case RIGHT:
                nextPosition = cpy.add(1, 0);
                break;
            case DOWN:
                nextPosition = cpy.add(0, -1);
                break;
            case LEFT:
                nextPosition = cpy.add(-1, 0);
                break;
            default:
        }

        return nextPosition;
    }

    public List<RoundEvents> getRoundEvents() {
        return roundEvents;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setConfused(boolean confused) {
        this.confused = confused;
    }

    public void setGhostMode() {
        this.ghostMode = true;
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
}
