package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.Agent;
import de.adesso.brainysnake.Gamelogic.Player.GameEvent;
import de.adesso.brainysnake.Gamelogic.Player.PlayerState;

public class GameMaster {

    List<Agent> agents = new ArrayList<Agent>();//TODO rukl@rukl change to dto or representation object. do not operate on real data
    private GameEvent agentActions;
    private Level level;


    public GameMaster(Level level) {
        this.level = level;
    }

    public void registerAgent(List<Agent> agents) {
        for (Agent agent : agents) {
            if (this.agents.contains(agent)) {
                Gdx.app.log("GameMaster", "Agent " + agent.getName() + "already registered");
                continue;
            }

            this.agents.add(agent);
            Gdx.app.log("GameMaster", "Register Agent " + agent.getName());
        }
    }

    public void getNextMoveActions() {
        for (Agent agent : agents) {
            GameEvent gameEvent = agent.generateMove(generatePlayerStatus(agent));
            if (validateGameEvent(gameEvent, agent)) {
                moveAgent(gameEvent, agent);
            }

        }

        // moveActions;
    }

    private void moveAgent(GameEvent gameEvent, Agent agent) {
        switch (gameEvent) {
            case MOVE_LEFT:
                agent.moveLeft();
                break;
            case MOVE_RIGHT:
                agent.moveRight();
                break;
            case MOVE_FORWARD:
                agent.moveForward();
                break;
        }
    }

    private boolean validateGameEvent(GameEvent gameEvent, Agent agent) {
        Dot headPosition = agent.getHeadPosition();
        boolean validEvent = true;

        switch (gameEvent) {
            case MOVE_LEFT:
              //  validEvent = level.checkCollision(headPosition.x, headPosition.y);
                break;
            case MOVE_RIGHT:
              //  validEvent = level.checkCollision(headPosition.x, headPosition.y);
                break;
            case MOVE_FORWARD:
               // validEvent = level.checkCollision(headPosition.x, headPosition.y);
                break;

            default:
                validEvent = false;
        }

        return validEvent;
    }


    private PlayerState generatePlayerStatus(Agent agent) {
        return null;//TODO rukl@rukl genearte status information for agent here
    }
}
