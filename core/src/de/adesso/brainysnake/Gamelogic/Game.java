package de.adesso.brainysnake.Gamelogic;


import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Player.Player1;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Level level;
    private Player1 player1;

    public void init(int levelHeight, int levelWidth) {
        level = new Level(levelHeight, levelWidth, Color.WHITE);
        player1 = new Player1();
    }

    public void update(float delta) {
        player1.update(delta);
    }

    public List<GameObject> draw(float delta) {
        List<GameObject> gameObjects = new ArrayList<GameObject>();
        gameObjects.add(level.getLevel());
        gameObjects.add(player1);
        return gameObjects;
    }

}
