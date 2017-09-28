package de.adesso.brainysnake.Gamelogic;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.Gamelogic.Player.Snake;

public class Game {

    private Level level;

    private GameMaster gameMaster;

    public void init(int levelHeight, int levelWidth) {
        level = new Level(levelHeight, levelWidth, Color.WHITE);
        gameMaster = new GameMaster(level);
    }

    public void update(float delta) {
        gameMaster.update(delta);
    }

    public List<GameObject> draw(float delta) {
        List<GameObject> gameObjects = new ArrayList<GameObject>();
        gameObjects.add(level.getLevel());
        gameObjects.add(level.getBarriers());
        gameObjects.add(level.getPoints());
        for (PlayerHandler playerHandler : gameMaster.getPlayerHandler()) {
            Snake snake = playerHandler.getSnake();
            gameObjects.add(snake.getHead());
            gameObjects.add(snake.getBody());
        }
        return gameObjects;
    }

}
