package de.adesso.brainysnake.Gamelogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerChoice;
import de.adesso.brainysnake.Gamelogic.Player.PlayerController;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.playercommon.RoundEvents;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;

import static de.adesso.brainysnake.playercommon.Orientation.*;
import static de.adesso.brainysnake.playercommon.RoundEvents.*;

public class GameMaster {

    // Alle Spiele erzeugen
    private BrainySnakePlayer playerOne = new SamplePlayer() {

        private boolean left, right, up, down;

        @Override
        public String getPlayerName() {
            return "SamplePlayer One";
        }

        @Override
        public PlayerUpdate tellPlayerUpdate() {
            if (KeyBoardControl.LEFT && !right) {
                left = true;
                right = up = down = false;
            }
            if (KeyBoardControl.RIGHT && !left) {
                right = true;
                left = up = down = false;
            }
            if (KeyBoardControl.UP && !down) {
                up = true;
                left = right = down = false;
            }
            if (KeyBoardControl.DOWN && !up) {
                down = true;
                left = right = up = false;
            }

            if (left) {
                return new PlayerUpdate(Orientation.LEFT);
            }
            if (right) {
                return new PlayerUpdate(Orientation.RIGHT);
            }
            if (up) {
                return new PlayerUpdate(Orientation.UP);
            }
            if (down) {
                return new PlayerUpdate(Orientation.DOWN);
            }

            return null;

        }
    };
    private BrainySnakePlayer playerTwo = new SamplePlayer() {

        @Override
        public String getPlayerName() {
            return "SamplePlayer Two";
        }

        @Override
        public PlayerUpdate tellPlayerUpdate() {
            return null;
        }
    };
    private BrainySnakePlayer playerThree = new SamplePlayer() {

        @Override
        public String getPlayerName() {
            return "SamplePlayer Three";
        }

        @Override
        public PlayerUpdate tellPlayerUpdate() {
            return new PlayerUpdate(RIGHT);
        }
    };
    private BrainySnakePlayer playerFour = new SamplePlayer() {

        @Override
        public String getPlayerName() {
            return "SamplePlayer Four";
        }

        @Override
        public PlayerUpdate tellPlayerUpdate() {
            return new PlayerUpdate(LEFT);
        }
    };

    // Add all Agents here
    private List<BrainySnakePlayer> brainySnakePlayers = new ArrayList<BrainySnakePlayer>();

    private PlayerController playerController;

    private Level level;

    public GameMaster(Level level) {
        // Create UI ?
        this.level = level;

        // Add agents to the game
        brainySnakePlayers.add(playerOne);
        brainySnakePlayers.add(playerTwo);
        brainySnakePlayers.add(playerThree);
        brainySnakePlayers.add(playerFour);

        // Build UI Models for the agents
        Map<Orientation, Snake> brainySnakePlayersUiModel = new HashMap<Orientation, Snake>();
        brainySnakePlayersUiModel.put(UP, level.createStartingGameObject(UP, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(DOWN, level.createStartingGameObject(DOWN, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(RIGHT, level.createStartingGameObject(RIGHT, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(LEFT, level.createStartingGameObject(LEFT, Config.INITIAL_PLAYER_LENGTH));

        // The PlayerController capsules agent actions an calculations
        // The Controller will randomly assign agents to GameObjects
        playerController = new PlayerController(brainySnakePlayers, brainySnakePlayersUiModel);
    }

    public void update(float delta) {
        gameLoop();
    }

    public void gameLoop() {

        // TODO rukl@rukl check timeOut

        // TODO Wir brauchen eine logische Sicht auf das Spiel.

        /**
         * Calculates the PlayerState and updates the Agents via call
         */
        this.playerController.updatePlayerState(new GlobalGameState());

        Map<PlayerHandler, PlayerChoice> playerStatus = this.playerController.getPlayerStatus();
        for (PlayerHandler playerHandler : playerStatus.keySet()) {
            PlayerChoice playerChoice = playerStatus.get(playerHandler);
            validateEvents(playerHandler, playerChoice);
        }

        // check react to gameevents of agents
        List<PlayerHandler> deadPlayer = new ArrayList<PlayerHandler>();
        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            List<RoundEvents> roundEvents = playerHandler.getRoundEvents();
            int collectedPoints = 0;
            for (RoundEvents roundEvent : roundEvents) {
                switch (roundEvent) {
                    case DIEDED:
                        deadPlayer.add(playerHandler);
                        break;
                    case MOVED:
                        // move player as he planned
                        playerHandler.moveToNextPosition();
                        break;
                    case CONFUSED:
                        collectedPoints++;
                        break;
                    case COLLISION_WITH_LEVEL:
                        collectedPoints--;
                        break;
                    case BIT_HIMSELF:
                        collectedPoints--;
                        break;
                    case BIT_AGENT:
                        if (!playerHandler.isGhostMode()) {
                            collectedPoints++;
                        }
                        break;
                    case BIT_BY_AGENT:
                        // agent was Hit by another agent
                        if (!playerHandler.isGhostMode()) {
                            collectedPoints--;
                        }
                        break;
                    case CONSUMED_POINT:
                        collectedPoints++;
                        break;
                }
            }

            // if no points where collected, just move the snake
            if (collectedPoints <= 0) {
                playerHandler.penalty();
                if (collectedPoints < -1) {
                    playerHandler.penalty();
                }
            }

            // TODO rukl@rukl wenn der agent an dieser Stelle nur noch einen Punkt hat stirbt er
            playerHandler.endround();
            playerHandler.update();
        }

        for (PlayerHandler dead : deadPlayer) {
            playerController.getPlayerHandlerList().remove(dead);
        }

        // setup Score for each agent;

        // spread new points in level
        level.spreadPoints();
    }

    private void validateEvents(PlayerHandler playerHandler, PlayerChoice playerChoice) {
        List<RoundEvents> roundEvents = playerHandler.getRoundEvents();

        if (playerHandler.isDead() || playerHandler.getSnake().countPoints() <= 1) {
            roundEvents.add(DIEDED);
            playerHandler.kill();
            return;
        }

        if (!playerChoice.isHasChosen() || !playerHandler.isOrientationValid(playerChoice.getOrientation())) {
            roundEvents.add(CONFUSED);
            playerHandler.setConfused(true);
            return;
        }

        Point2D nextPosition = playerHandler.getNextPositionBy(playerChoice.getOrientation());
        if (level.checkCollision(nextPosition.x, nextPosition.y)) {
            roundEvents.add(COLLISION_WITH_LEVEL);
            playerHandler.setConfused(true);
            return;
        }

        playerHandler.setCurrentOrientation(playerChoice.getOrientation());
        roundEvents.add(MOVED);
        playerHandler.setConfused(false);

        // did the player bit any snake object
        for (PlayerHandler player : playerController.getPlayerHandlerList()) {
            if (!playerHandler.isGhostMode() && player.gotBitten(nextPosition)) {
                if (player.equals(playerHandler)) {
                    roundEvents.add(BIT_HIMSELF);
                } else {
                    roundEvents.add(BIT_AGENT);
                    player.getRoundEvents().add(BIT_BY_AGENT);
                }
                playerHandler.setGhostMode();
            }
        }

        if (level.tryConsumePoint(nextPosition)) {
            roundEvents.add(CONSUMED_POINT);
        }
    }

    public List<PlayerHandler> getPlayerHandler() {
        return playerController.getPlayerHandlerList();
    }

    public void shutdown() {
        this.playerController.shutdown();
    }
}
