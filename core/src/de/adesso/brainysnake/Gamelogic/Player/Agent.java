package de.adesso.brainysnake.Gamelogic.Player;


import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;

import static de.adesso.brainysnake.Gamelogic.Player.Orientation.*;

public class Agent extends GameObject {

    private static String name = "AgentDummy";

    private boolean left, right, up, down;

    private Orientation[] orientations       = new Orientation[]{UP, RIGHT, DOWN, LEFT};
    private int           currentOrientation = 0;

    private boolean dead = false;

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
        }

        updateInput();
    }

    public GameEvent generateMove(PlayerState playerState) {
        update(0);

        GameEvent gameEvent;

        if (left) {
            gameEvent = GameEvent.MOVE_LEFT;
        } else if (right) {
            gameEvent = GameEvent.MOVE_RIGHT;
        } else {
            gameEvent = GameEvent.MOVE_FORWARD;

        }

        left = right = false;
        up = true;
        return gameEvent;
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

    public Dot getHeadPosition() {
        return dots.get(dots.size() - 1);
    }

    public void moveRight() {
        if (++currentOrientation >= orientations.length) {
            currentOrientation = 0;
        }

        moveForward();
    }

    public void moveLeft() {
        if (--currentOrientation < 0) {
            currentOrientation = orientations.length - 1;
        }

        moveForward();
    }

    public void moveForward() {
        switch (orientations[currentOrientation]) {
            case UP:
                dots.add(new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y + 1));
                break;
            case RIGHT:
                dots.add(new Dot(dots.get(dots.size() - 1).x + 1, dots.get(dots.size() - 1).y));
                break;
            case DOWN:
                dots.add(new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y - 1));
                break;
            case LEFT:
                dots.add(new Dot(dots.get(dots.size() - 1).x - 1, dots.get(dots.size() - 1).y));
                break;
            default:

        }
    }
}
