package de.adesso.brainysnake.Gamelogic.Player;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.playercommon.GameEvent;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.playercommon.math.Point2D;

import static de.adesso.brainysnake.playercommon.Orientation.*;

public class Agent extends GameObject {

    private final Color playerColor;

    private BrainySnakePlayer brainySnakePlayer;

    private PlayerState playerState;

    private boolean left, right, up, down;

    private final static Orientation[] orientations = new Orientation[]{UP, RIGHT, DOWN, LEFT};

    private int currentOrientation = 0;

    private int ghostTime = 0;

    private boolean dead, ghostMode, confused = false;

    private List<GameEvent> gameEvents = new ArrayList<GameEvent>();

    private AgentMovement agentMovement;

    private boolean keyboardControlled = false;

    public Agent(BrainySnakePlayer brainySnakePlayer, Color playerColor, boolean keyboardControlled) {
        super();
        this.brainySnakePlayer = brainySnakePlayer;
        this.keyboardControlled = keyboardControlled;
        this.playerColor = playerColor;

        positions.add(new Point2D(50, 50));
        positions.add(new Point2D(50, 51));
        positions.add(new Point2D(50, 52));
        positions.add(new Point2D(50, 53));
        positions.add(new Point2D(50, 54));
        positions.add(new Point2D(50, 55));
        positions.add(new Point2D(50, 56));
        positions.add(new Point2D(50, 57));
        positions.add(new Point2D(50, 58));
        positions.add(new Point2D(50, 59));
        positions.add(new Point2D(50, 60));
        positions.add(new Point2D(50, 61));
        positions.add(new Point2D(50, 62));
        positions.add(new Point2D(50, 63));
        positions.add(new Point2D(50, 64));
        positions.add(new Point2D(50, 65));
        positions.add(new Point2D(50, 66));
        positions.add(new Point2D(50, 67));
        positions.add(new Point2D(50, 68));
    }

    private void update(float delta) {

        if (dead) {
            color = Config.DEAD_COLOR;
            return;
        }

        if (ghostMode) {
            ghostMode();
        }else if (confused) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }

        updateInput();
    }

    public void updatePlayerState(GameEvent gameEvent) {
        new PlayerState(0, 0, 0, positions.get(positions.size() - 1), positions.get(0), false, null, ghostMode, null);
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    private void ghostMode() {
        if (ghostTime++ > Config.GHOST_TIME) {
            ghostTime = 0;
            color = playerColor;
            ghostMode = false;
        } else {
            color = Config.GHOST_MODE_COLOR;
        }
    }

    public void generateMove() {
        update(0);

        //TODO rukl@rukl PlayerUpdate durch agentMovement funktionalität ersetzen oder damit verheiraten
        if (!keyboardControlled) {
            PlayerUpdate playerUpdate = brainySnakePlayer.tellPlayerUpdate();
            return;
        }

        AgentMovement agentMovement = AgentMovement.MOVE_FORWARD;

        if (right) {
            if (currentOrientation == 0) {
                agentMovement = AgentMovement.MOVE_RIGHT;
            } else if (currentOrientation == 2) {
                agentMovement = AgentMovement.MOVE_LEFT;
            }
        }

        if (left) {
            if (currentOrientation == 0) {
                agentMovement = AgentMovement.MOVE_LEFT;
            } else if (currentOrientation == 2) {
                agentMovement = AgentMovement.MOVE_RIGHT;
            }
        }

        if (up) {
            if (currentOrientation == 1) {
                agentMovement = AgentMovement.MOVE_LEFT;
            } else if (currentOrientation == 3) {
                agentMovement = AgentMovement.MOVE_RIGHT;
            }
        }

        if (down) {
            if (currentOrientation == 1) {
                agentMovement = AgentMovement.MOVE_RIGHT;
            } else if (currentOrientation == 3) {
                agentMovement = AgentMovement.MOVE_LEFT;
            }
        }

        /*
        if (left) {
            agentMovement = AgentMovement.MOVE_LEFT;
        } else if (right) {
            agentMovement = AgentMovement.MOVE_RIGHT;
        } else {
            agentMovement = AgentMovement.MOVE_FORWARD;
        }
        */

        left = right = up = false;
        this.agentMovement = agentMovement;
    }



    private void updateInput() {
        if (KeyBoardControl.LEFT && !right) {
            left = true;
            right = up = down = false;
        }
        if (KeyBoardControl.RIGHT && !left) {
            right = true;
            left = up = down = false;
        }
        if (KeyBoardControl.UP && !down) {
            up = true;
            left = right = down = false;
        }
        if (KeyBoardControl.DOWN && !up) {
            down = true;
            left = right = up = false;
        }
    }

    private void spinRight() {
        if (++currentOrientation >= orientations.length) {
            currentOrientation = 0;
        }
    }

    private void spinLeft() {
        if (--currentOrientation < 0) {
            currentOrientation = orientations.length - 1;
        }
    }

    /**
     * What is the next Agent Head Position depending on the next GameEvent
     *
     * @return
     */
    public Point2D getNextPosition() {
        int saveCurrentOrientation = currentOrientation;
        Point2D nextPosition = nextPositionIs(agentMovement);
        currentOrientation = saveCurrentOrientation;
        return nextPosition;
    }

    public void moveToNextPosition() {
        positions.add(nextPositionIs(agentMovement));
        confused = false;
    }

    private void spin(AgentMovement agentMovement) {
        switch (agentMovement) {
            case MOVE_RIGHT:
                spinRight();
                break;
            case MOVE_LEFT:
                spinLeft();
                break;
            default:
        }
    }

    private Point2D nextPositionIs(AgentMovement agentMovement) {
        spin(agentMovement);
        Point2D nextPosition = null;
        switch (orientations[currentOrientation]) {
            case UP:
                nextPosition = new Point2D(positions.get(positions.size() - 1).x, positions.get(positions.size() - 1).y + 1);
                break;
            case RIGHT:
                nextPosition = new Point2D(positions.get(positions.size() - 1).x + 1, positions.get(positions.size() - 1).y);
                break;
            case DOWN:
                nextPosition = new Point2D(positions.get(positions.size() - 1).x, positions.get(positions.size() - 1).y - 1);
                break;
            case LEFT:
                nextPosition = new Point2D(positions.get(positions.size() - 1).x - 1, positions.get(positions.size() - 1).y);
                break;
            default:
        }

        return nextPosition;
    }

    public void removeTail() {
        positions.remove(0);
    }

    public boolean isConfused() {
        return confused;
    }

    public void setConfused(boolean isConfused) {
        this.confused = isConfused;
    }

    public boolean containsPosition(Point2D position) {
        for (Point2D dot : this.positions) {
            if (position.x == dot.x && position.y == dot.y) {
                return true;
            }
        }
        return false;
    }

    public List<GameEvent> getGameEvents() {
        return gameEvents;
    }

    public void endround() {
        gameEvents.clear();
        agentMovement = null;
    }

    public void setGhostMode() {
        ghostMode = true;
        ghostTime = 0;
    }

    public boolean isGhostMode() {
        return ghostMode;
    }

    public void kill(){
        dead = true;
        Gdx.app.log("AGENT: " , "Player " + brainySnakePlayer.getPlayerName() + " has dieded");
    }

    public boolean isDead(){
        return dead;
    }

    public BrainySnakePlayer getBrainySnakePlayer() {
        return brainySnakePlayer;
    }
}
