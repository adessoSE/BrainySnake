package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.*;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;

import static de.adesso.brainysnake.Gamelogic.Player.Orientation.*;

public class GameMaster {

    //Alle Spiele erzeugen
    private BrainySnakePlayer playerOne = new SamplePlayer();
    private BrainySnakePlayer playerTwo = new SamplePlayer();
    private BrainySnakePlayer playerThree = new SamplePlayer();
    private BrainySnakePlayer playerFour = new SamplePlayer();

    List<AgentPlayer> agentPlayerList = new ArrayList<AgentPlayer>();

    List<Agent> agents = new ArrayList<Agent>();
    //TODO rukl@rukl change to dto or representation object. do not operate on real data
    private Level         level;


    public GameMaster(Level level) {
        this.level = level;

        // TODO ftk@rukl AgentPlayer ist wie dein Agent.class wird hier nur mit der Implementierung sowie der Startposition initialisiert
        // Darfst du aber gerne so benennen wie du willst :)
        agentPlayerList = new ArrayList<AgentPlayer>();
        agentPlayerList.add(new AgentPlayer(playerOne, UP, level.createStartingGameObject(UP, Config.INITIAL_PLAYER_LENGTH, Color.BLUE)));
        agentPlayerList.add(new AgentPlayer(playerOne, RIGHT, level.createStartingGameObject(RIGHT, Config.INITIAL_PLAYER_LENGTH, Color.GREEN)));
        agentPlayerList.add(new AgentPlayer(playerOne, DOWN, level.createStartingGameObject(UP, Config.INITIAL_PLAYER_LENGTH, Color.ORANGE)));
        agentPlayerList.add(new AgentPlayer(playerOne, LEFT, level.createStartingGameObject(LEFT, Config.INITIAL_PLAYER_LENGTH, Color.CYAN)));
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
