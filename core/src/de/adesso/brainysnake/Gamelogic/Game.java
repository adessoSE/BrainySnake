package de.adesso.brainysnake.Gamelogic;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.Agent;

public class Game {

    private Level      level;
    private GameMaster gameMaster;
    private List<Agent> agents = new ArrayList<Agent>();


    public void init(int levelHeight, int levelWidth) {
        level = new Level(levelHeight, levelWidth, Color.WHITE);
        gameMaster = new GameMaster(level);
        agents.add(new Agent());
        gameMaster.registerAgent(agents);
    }

    public void update(float delta) {
        gameMaster.update(delta);
    }

    public List<GameObject> draw(float delta) {
        List<GameObject> gameObjects = new ArrayList<GameObject>();
        gameObjects.add(level.getLevel());
        for (Agent agent : agents) {
            gameObjects.add(agent);
        }
        return gameObjects;
    }

}
