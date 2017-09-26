package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.Agent;
import de.adesso.brainysnake.Gamelogic.Player.AgentMovement;
import de.adesso.brainysnake.Gamelogic.Player.PlayerState;

public class GameMaster {

    List<Agent> agents = new ArrayList<Agent>();
    //TODO rukl@rukl change to dto or representation object. do not operate on real data
    private Level         level;


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

    public void update(float delta) {
        //check timeout
        getNextMoveActions();
    }

    public void getNextMoveActions() {

        //update playerstate for each agent
        for (Agent agent : agents) {
            agent.updatePlayerState(generatePlayerStatus(agent));
        }

        //collect agent movements
        Map<Agent, AgentMovement> agentMovements = new HashMap<Agent, AgentMovement>();
        for (Agent agent : agents) {
            agentMovements.put(agent, agent.generateMove());
        }

        //check validity of agent movements and moveToNextPosition, if action is valid
        for (Agent agent : agentMovements.keySet()) {
            AgentMovement agentMovement = agentMovements.get(agent);
            GameEvent     movementValid = isMovementValid(agentMovement, agent);

            //moveToNextPosition ist valid
            if (movementValid.equals(GameEvent.MOVED)) {
                agent.moveToNextPosition(agentMovement);
                agent.setConfused(false);
            } else {
                agent.setConfused(true);
            }
        }
        agentMovements.clear();

        // setup Score for each agent;


    }

    private GameEvent isMovementValid(AgentMovement agentMovement, Agent agent) {
        GameEvent gameEvent = GameEvent.MOVED;

        Dot nextPosition = agent.getNextPosition(agentMovement);
        if (level.checkCollision(nextPosition.x, nextPosition.y)) {
            gameEvent = GameEvent.COLLISION_WITH_LEVEL;
        } else {
            //TODO rukl@rukl check collision with barrier
        }

        return gameEvent;
    }

    private PlayerState generatePlayerStatus(Agent agent) {
        return null;//TODO rukl@rukl genearte status information for agent here
    }
}
