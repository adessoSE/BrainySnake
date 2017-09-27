package de.adesso.brainysnake.Gamelogic.Player;


import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.GameEvent;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerUpdate;

import java.util.ArrayList;
import java.util.List;

import static de.adesso.brainysnake.Gamelogic.Player.Orientation.*;

public class Agent extends GameObject {

    private final Color playerColor;

    private BrainySnakePlayer brainySnakePlayer;

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

        dots.add(new Dot(50, 50));
        dots.add(new Dot(50, 51));
        dots.add(new Dot(50, 52));
        dots.add(new Dot(50, 53));
        dots.add(new Dot(50, 54));
        dots.add(new Dot(50, 55));
        dots.add(new Dot(50, 56));
        dots.add(new Dot(50, 57));
        dots.add(new Dot(50, 58));
        dots.add(new Dot(50, 59));
        dots.add(new Dot(50, 60));
        dots.add(new Dot(50, 61));
        dots.add(new Dot(50, 62));
        dots.add(new Dot(50, 63));
        dots.add(new Dot(50, 64));
        dots.add(new Dot(50, 65));
        dots.add(new Dot(50, 66));
        dots.add(new Dot(50, 67));
        dots.add(new Dot(50, 68));
    }

    private void update(float delta) {

        if (ghostMode) {
            ghostMode();
        }else if (confused) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }

        updateInput();
    }

    public void updatePlayerState() {
        //TODO rukl@rukl save playerstate
    }

    private void blink(float delta) {
     /*   if (currentBlinkLength++ > blinkingSpeed) {
            currentBlinkLength = 0;
            color = Config.DEFAULT_PLAYER_COLOR;
        } else {
            color = Config.BLINK_COLOR;
        }*/
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
    public Dot getNextPosition() {
        int saveCurrentOrientation = currentOrientation;
        Dot nextPosition = nextPositionIs(agentMovement);
        currentOrientation = saveCurrentOrientation;
        return nextPosition;
    }

    public void moveToNextPosition() {
        dots.add(nextPositionIs(agentMovement));
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

    private Dot nextPositionIs(AgentMovement agentMovement) {
        spin(agentMovement);
        Dot nextPosition = null;
        switch (orientations[currentOrientation]) {
            case UP:
                nextPosition = new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y + 1);
                break;
            case RIGHT:
                nextPosition = new Dot(dots.get(dots.size() - 1).x + 1, dots.get(dots.size() - 1).y);
                break;
            case DOWN:
                nextPosition = new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y - 1);
                break;
            case LEFT:
                nextPosition = new Dot(dots.get(dots.size() - 1).x - 1, dots.get(dots.size() - 1).y);
                break;
            default:
        }

        return nextPosition;
    }

    public void removeTail() {
        dots.remove(0);
    }

    public boolean isConfused() {
        return confused;
    }

    public void setConfused(boolean isConfused) {
        this.confused = isConfused;
    }

    public boolean containsPosition(Dot position) {
        for (Dot dot : dots) {
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
}
