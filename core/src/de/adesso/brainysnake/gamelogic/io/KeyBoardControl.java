package de.adesso.brainysnake.gamelogic.io;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class KeyBoardControl extends InputAdapter {

    public static boolean SPACE;

    public static boolean UP, LEFT, DOWN, RIGHT;

    public static boolean W, A, S, D;

    public boolean keyDown(int k) {
        if (k == Input.Keys.LEFT) {
            LEFT = true;
            RIGHT = UP = DOWN = false;
        }
        if (k == Input.Keys.RIGHT) {
            RIGHT = true;
            LEFT = UP = DOWN = false;
        }
        if (k == Input.Keys.UP) {
            UP = true;
            RIGHT = LEFT = DOWN = false;
        }
        if (k == Input.Keys.DOWN) {
            DOWN = true;
            RIGHT = UP = LEFT = false;
        }
        if (k == Input.Keys.SPACE) {
            SPACE = true;
        }
        if (k == Input.Keys.A) {
            A = true;
            W = S = D = false;
        }
        if (k == Input.Keys.D) {
            D = true;
            W = A = S = false;
        }
        if (k == Input.Keys.W) {
            W = true;
            A = S = D = false;
        }
        if (k == Input.Keys.S) {
            S = true;
            A = W = D = false;
        }
        return true;
    }

    public boolean keyUp(int k) {
        if (k == Input.Keys.LEFT) {
            LEFT = false;
        }
        if (k == Input.Keys.RIGHT) {
            RIGHT = false;
        }
        if (k == Input.Keys.UP) {
            UP = false;
        }
        if (k == Input.Keys.DOWN) {
            DOWN = false;
        }
        if (k == Input.Keys.SPACE) {
            SPACE = false;
        }
        if (k == Input.Keys.S) {
            S = false;
        }
        if (k == Input.Keys.W) {
            W = false;
        }
        if (k == Input.Keys.A) {
            A = false;
        }
        if (k == Input.Keys.D) {
            D = false;
        }
        return true;
    }

}
