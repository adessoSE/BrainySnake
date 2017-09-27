package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.Agent;
import de.adesso.brainysnake.Gamelogic.Player.AgentMovement;
import de.adesso.brainysnake.Gamelogic.Player.AgentPlayer;
import de.adesso.brainysnake.Gamelogic.Player.PlayerState;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;

import static de.adesso.brainysnake.Gamelogic.Player.Orientation.*;

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
    List<Agent> deadAgents = new ArrayList<Agent>();

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

    public void registerPlayer() {
        agents.add(new Agent(playerOne, new Color(124f, 123f, 123f, 255f), true));
    }

    public void update(float delta) {
        gameLoop();
    }

    public void gameLoop() {

        //TODO rukl@rukl check timeOut

        //update playerstate for each agent
        for (Agent agent : agents) {
            agent.updatePlayerState();
        }

        // TODO Wir brauchen eine logische Sicht auf das Spiel.
        /**
         * Calculates the PlayerState and updates the Agents via call
         */
        this.playerController.updatePlayerState(new GlobalGameState());

        //collect agent movements
        for (Agent agent : agents) {
            agent.generateMove();
        }

        Map<PlayerHandler, AgentChoice> playerStatus = this.playerController.getPlayerStatus();
        for(PlayerHandler playerHandler : playerStatus.keySet()) {

        }


        //check validity of agent movements and moveToNextPosition, if action is valid
        for (Agent agent : agents) {
            validateEvents(agent);
        }

        //check react to gameevents of agents
        for (Agent agent : agents) {
            List<GameEvent> gameEvents = agent.getGameEvents();
            int collectedPoints = 0;
            for (GameEvent gameEvent : gameEvents) {
                switch (gameEvent) {
                    case DIEDED:
                        agent.kill();
                        deadAgents.add(agent);
                        break;
                    case MOVED:
                        //move player as he planned
                        agent.moveToNextPosition();
                        break;
                    case COLLISION_WITH_LEVEL:
                        agent.setConfused(true);
                        collectedPoints--;
                        break;
                    case HIT_HIMSELF:
                        collectedPoints--;
                        agent.setGhostMode();
                        break;
                    case HIT_AGENT:
                        agent.setConfused(true);
                        collectedPoints++;
                        break;
                    case HIT_BY_AGENT:
                        //agent was Hit by another agent
                        if (!agent.isGhostMode()) {
                            collectedPoints--;
                        }
                        agent.setGhostMode();
                        break;
                    case CONSUMED_POINT:
                        collectedPoints++;
                        break;
                }
            }

            //if no points where collected, just move the snake
            if (collectedPoints <= 0) {
                agent.removeTail();
                //if negative points where collected, remove max one point
                if (collectedPoints <= -1) {
                    agent.removeTail();
                }
            }

            //TODO rukl@rukl wenn der agent an dieser Stelle nur noch einen Punkt hat stirbt er
            agent.endround();
        }


        for (Agent agent : deadAgents) {
            agents.remove(agent);
        }


        // setup Score for each agent;


        // spread new points in level
        level.spreadPoints();
    }

    private void validateEvents(Agent agent) {

        if (agent.getDots().size() <= 1) {
            agent.getGameEvents().add(GameEvent.DIEDED);
            return;
        }

        Dot nextPosition = agent.getNextPosition();
        if (level.checkCollision(nextPosition.x, nextPosition.y)) {
            agent.getGameEvents().add(GameEvent.COLLISION_WITH_LEVEL);
            return;
        }

        agent.getGameEvents().add(GameEvent.MOVED);

        //did the agent hit any snake object
        for (Agent tempAgent : agents) {
            if (tempAgent.containsPosition(nextPosition)) {
                if (tempAgent.equals(agent)) {
                    agent.getGameEvents().add(GameEvent.HIT_HIMSELF);
                } else {
                    agent.getGameEvents().add(GameEvent.HIT_AGENT);
                    tempAgent.getGameEvents().add(GameEvent.HIT_BY_AGENT);
                }
            }
        }

        if (level.tryConsumePoint(nextPosition)) {
            agent.getGameEvents().add(GameEvent.CONSUMED_POINT);
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void shutdown() {
        this.playerController.shutdown();
    }
}
