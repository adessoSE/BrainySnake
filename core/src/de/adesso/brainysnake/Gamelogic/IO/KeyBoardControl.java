package de.adesso.brainysnake.Gamelogic.IO;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.adesso.brainysnake.Gamelogic.Game;

public class KeyBoardControl extends InputAdapter {

    public Game game;

    public static boolean LEFT;

    public static boolean RIGHT;

    public static boolean UP;

    public static boolean DOWN;

    public KeyBoardControl(Game game){
        this.game = game;
    }

    public boolean keyDown(int k) {
        if (k == Input.Keys.LEFT){
            LEFT = true;
            RIGHT = UP = DOWN = false;
        }
        if (k == Input.Keys.RIGHT){
            RIGHT = true;
            LEFT = UP = DOWN = false;
        }
        if (k == Input.Keys.UP){
            UP = true;
            RIGHT = LEFT = DOWN = false;
        }
        if (k == Input.Keys.DOWN){
            DOWN = true;
            RIGHT = UP = LEFT = false;
        }
        if (k == Input.Keys.SPACE ){
            //TODO rukl@rukl restart
        }
        return true;
    }

    public boolean keyUp(int k) {
        if (k == Input.Keys.LEFT){
            LEFT = false;
        }
        if (k == Input.Keys.RIGHT){
            RIGHT = false;
        }
        if (k == Input.Keys.UP){
            UP = false;
        }
        if (k == Input.Keys.DOWN){
            DOWN = false;
        }
        return true;
    }
}
