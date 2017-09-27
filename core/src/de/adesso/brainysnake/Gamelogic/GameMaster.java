package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.Agent;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;

public class GameMaster {

    //Alle Spiele erzeugen
    private BrainySnakePlayer playerOne = new SamplePlayer();
    private BrainySnakePlayer playerTwo = new SamplePlayer();
    List<Agent> agents = new ArrayList<Agent>();

    //TODO rukl@rukl change to dto or representation object. do not operate on real data
    private Level level;

    public GameMaster(Level level) {
        this.level = level;
    }

    public void registerPlayer() {
        agents.add(new Agent(playerOne, new Color(124f, 123f, 123f, 255f), true));
    }

    public void update(float delta) {
        //check timeout
        getNextMoveActions();
    }

    public void getNextMoveActions() {

        //update playerstate for each agent
        for (Agent agent : agents) {
            agent.updatePlayerState();
        }

        //collect agent movements
        for (Agent agent : agents) {
            agent.generateMove();
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
                        //agent.
                    case DIEDED:
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

        // setup Score for each agent;


        // spread new points in level
        level.spreadPoints();
    }

    private void validateEvents(Agent agent) {
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
}
