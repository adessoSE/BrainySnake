package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.*;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;

import static de.adesso.brainysnake.playercommon.Orientation.*;

public class GameMaster {

    //Alle Spiele erzeugen
    private BrainySnakePlayer playerOne = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer One";
        }
    };
    private BrainySnakePlayer playerTwo  = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer Two";
        }
    };
    private BrainySnakePlayer playerThree = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer Three";
        }
    };
    private BrainySnakePlayer playerFour  = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer Four";
        }
    };

    // Add all Agents here
    List<BrainySnakePlayer> brainySnakePlayers = new ArrayList<BrainySnakePlayer>();

    PlayerController playerController;

    List<Agent> agents = new ArrayList<Agent>();

    //TODO rukl@rukl change to dto or representation object. do not operate on real data
    private Level level;


    public GameMaster(Level level) {
        // Create UI ?
        this.level = level;

        //Add agents to the game
        brainySnakePlayers.add(playerOne);
        brainySnakePlayers.add(playerTwo);
        brainySnakePlayers.add(playerThree);
        brainySnakePlayers.add(playerFour);

        // Build UI Models for the agents
        Map<Orientation, GameObject> brainySnakePlayersUiModel = new HashMap<Orientation, GameObject>();
        brainySnakePlayersUiModel.put(UP, level.createStartingGameObject(UP, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(DOWN, level.createStartingGameObject(DOWN, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(RIGHT,level.createStartingGameObject(RIGHT, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(LEFT,level.createStartingGameObject(LEFT, Config.INITIAL_PLAYER_LENGTH));

        // The PlayerController capsules agent actions an calculations
        // The Controller will randomly assign agents to GameObjects
        playerController = new PlayerController(brainySnakePlayers, brainySnakePlayersUiModel);
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
//        for (Agent agent : agents) {
//            agent.updatePlayerState(generatePlayerStatus(agent));
//        }

        // TODO Wir brauchen eine logische Sicht auf das Spiel.
        /**
         * Calculates the PlayerState and updates the Agents via call
         */
        this.playerController.updatePlayerState(new GlobalGameState());

        //collect agent movements
        Map<Agent, AgentMovement> agentMovements = new HashMap<Agent, AgentMovement>();
        for (Agent agent : agents) {
            agentMovements.put(agent, agent.generateMove());
        }

        Map<PlayerHandler, AgentChoice> playerStatus = this.playerController.getPlayerStatus();
        for(PlayerHandler playerHandler : playerStatus.keySet()) {

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

        // spread new points in level
        level.spreadPoints();


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

    public void shutdown() {
        this.playerController.shutdown();
    }
}
