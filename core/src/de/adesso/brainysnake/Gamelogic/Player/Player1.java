package de.adesso.brainysnake.Gamelogic.Player;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;

public class Player1 extends GameObject {

    private boolean left, right, up, down;

    private boolean dead = false;

    private int currentBlinkLength = 0;

    private int blinkingSpeed = Config.BLINKING_SPEED;


    public Player1() {
        super();
        dots.add(new Dot(50, 50));
        dots.add(new Dot(50, 51));
        dots.add(new Dot(50, 52));
        dots.add(new Dot(50, 53));
        dots.add(new Dot(50, 54));
        up = true;
        down = left = right = false;
    }


    public void update(float delta) {

        if (dead) {
            blink(delta);
            return;
        }

        updateInput();
        handleInput();
        dead = checkCollision();
    }

    private boolean checkCollision() {
        //TODO rukl@rukl check collisions
        return false;
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

    /**
     * TODO rukl@rukl DOC
     */
    private void handleInput() {

        if (left){
            dots.add(new Dot(dots.get(dots.size() - 1).x - 1, dots.get(dots.size() - 1).y));
        }
        if (right){
            dots.add(new Dot(dots.get(dots.size() - 1).x + 1, dots.get(dots.size() - 1).y));
        }
        if (up){
            dots.add(new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y + 1));
        }
        if (down){
            dots.add(new Dot(dots.get(dots.size() - 1).x, dots.get(dots.size() - 1).y - 1));
        }
        dots.remove(0);
    }

}
