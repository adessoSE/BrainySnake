package de.adesso.brainysnake.Gamelogic.Player;


import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.Orientation;

import static de.adesso.brainysnake.playercommon.Orientation.*;

public class Agent extends GameObject {

    private static String name = "AgentDummy";

    private boolean left, right, up, down;

    private Orientation[] orientations       = new Orientation[]{UP, RIGHT, DOWN, LEFT};

    private int           currentOrientation = 0;

    private boolean dead, frozen, confused = false;

    private int currentBlinkLength = 0;

    private int blinkingSpeed = Config.BLINKING_SPEED;

    public Agent() {
        super();
        dots.add(new Dot(50, 50));
        dots.add(new Dot(50, 51));
        dots.add(new Dot(50, 52));
        dots.add(new Dot(50, 53));
        dots.add(new Dot(50, 54));
        up = true;
        down = left = right = false;
    }

    private void update(float delta) {

        if (dead) {
            blink(delta);
            return;
        } else if (confused) {
            color = Color.YELLOW;
        } else {
            color = Color.RED;
        }

        updateInput();
    }

    public void updatePlayerState(PlayerState playerState) {
        //TODO rukl@rukl save playerstate
    }

    public AgentMovement generateMove() {
        update(0);

        AgentMovement agentMovement;

        if (left) {
            agentMovement = AgentMovement.MOVE_LEFT;
        } else if (right) {
            agentMovement = AgentMovement.MOVE_RIGHT;
        } else {
            agentMovement = AgentMovement.MOVE_FORWARD;
        }

        left = right = false;
        up = true;
        return agentMovement;
    }

    private void blink(float delta) {
        if (currentBlinkLength++ > blinkingSpeed) {
            currentBlinkLength = 0;
            color = Config.DEFAULT_PLAYER_COLOR;
        } else {
            color = Config.TWINKLE_COLOR;
        }
    }

    /**
     * TODO rukl@rukl DOC
     */
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

    public String getName() {
        return name;
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
     * @param agentMovement
     *
     * @return
     */
    public Dot getNextPosition(AgentMovement agentMovement) {
        int saveCurrentOrientation = currentOrientation;
        Dot nextPosition           = nextPositionIs(agentMovement);
        currentOrientation = saveCurrentOrientation;
        return nextPosition;
    }

    public void moveToNextPosition(AgentMovement agentMovement) {
        dots.add(nextPositionIs(agentMovement));
        dots.remove(dots.get(0));
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

    public void setConfused(boolean isConfused) {
        this.confused = isConfused;
    }

}
