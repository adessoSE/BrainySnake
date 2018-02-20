package de.adesso.brainysnake.Gamelogic;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerChoice;
import de.adesso.brainysnake.Gamelogic.Player.PlayerController;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.Gamelogic.UI.UiState;
import de.adesso.brainysnake.playercommon.Field;
import de.adesso.brainysnake.playercommon.FieldType;
import de.adesso.brainysnake.playercommon.PlayerView;
import de.adesso.brainysnake.playercommon.RoundEvent;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.adesso.brainysnake.playercommon.RoundEvent.*;

/**
 * Takes over the role of the game director. Know the rules of the game and take on professional validation, which concerns the game logic.
 */
public class GameMaster {

    private PlayerController playerController;

    private Level level;

    private boolean gameOver;

    private GameBoard gameBoard;

    public GameMaster(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void initialize(Level level){
        this.level = level;
        playerController = new PlayerController(gameBoard.getBrainySnakePlayers(), level);
    }

    public void update(float delta) {
        gameLoop();
    }

    public void gameLoop() {

        GlobalGameState.countMoves++;

        UiState.getINSTANCE().setRoundsRemaining(GlobalGameState.movesRemaining());

        //check if game is over
        if (!checkIfPlayerWon().isEmpty()) {
            gameOver = true;
            ScreenManager.getINSTANCE().showScreen(ScreenType.GAME_OVER_SCREEN);
        }

        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            //update view of player
            updateRoundForPlayer(playerHandler);

            // calculates the playerState and updates the playercontroller via call
            playerController.updatePlayerState();
        }

        Map<PlayerHandler, PlayerChoice> playerStatus = playerController.getPlayerStatus();
        for (PlayerHandler playerHandler : playerStatus.keySet()) {
            PlayerChoice playerChoice = playerStatus.get(playerHandler);
            validateEvents(playerHandler, playerChoice);
        }
        playerStatus.clear();

        // check reaction to game events of agents
        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            List<RoundEvent> roundEvents = playerHandler.getRoundEvents();
            int collectedPoints = 0;
            for (RoundEvent roundEvent : roundEvents) {
                switch (roundEvent) {
                    case DIED:
                        playerHandler.getSnake().removeHead();
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
                    case BIT_BY_PLAYER:
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
                // if negative points where collected, add another penalty
                if (collectedPoints <= -1) {
                    playerHandler.penalty();
                }
            }
        }

        playerController.removeDeadPlayer();


        // spread new points in level
        level.spreadPoints();

        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            // reset data of player
            playerHandler.endRound();
        }

        //TODO rukl update uistate
        // UiState.getINSTANCE().updatePlayerPoints(playerHandler.getPlayerName(), new UIPlayerInformation(playerHandler.getSnake().getHeadColor(), playerHandler.getSnake().countPoints()));
    }

    private void updateRoundForPlayer(PlayerHandler playerHandler) {
        List<Point2D> playerViewPositions = PlayerViewHelper.generatePlayerView(playerHandler.getCurrentOrientation(), playerHandler.getHeadPosition());
        List<Point2D> playerPositions = playerController.getPlayerPositions();
        List<Field> playerView = new ArrayList<>();
        for (Point2D point2D : playerViewPositions) {
            if (!level.levelContainsPosition(point2D)) {
                playerView.add(new Field(point2D, FieldType.NONE));
                continue;
            }

            if (level.checkCollision(point2D)) {
                playerView.add(new Field(point2D, FieldType.LEVEL));
            } else if (level.isPointOn(point2D)) {
                playerView.add(new Field(point2D, FieldType.POINT));
            } else if (playerPositions.contains(point2D)) {
                playerView.add(new Field(point2D, FieldType.PLAYER));
            } else {
                playerView.add(new Field(point2D, FieldType.EMPTY));
            }

        }

        playerHandler.updatePlayerView(new PlayerView(playerView, playerHandler.getCurrentOrientation(), Config.PLAYERVIEW_OFFSET_TO_VIEWWIDTH, Config.PLAYERVIEW_OFFSET_TO_AHEAD));
        playerHandler.update();
    }

    /**
     * Check if a player has won the game. When the time is up, the player with the most points has won.
     * If there is only one snake left in the season, it has won.
     * If there is a tie there can be more than one winner.
     *
     * @return Winner als {@link List} of {@link PlayerHandler}
     */
    public List<PlayerHandler> checkIfPlayerWon() {
        List<PlayerHandler> winner = new ArrayList<>();
        if (GlobalGameState.movesRemaining() <= 0) {

            int maxPoints = -1;
            for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
                maxPoints = Math.max(maxPoints, playerHandler.getSnake().countPoints());
            }

            for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
                if (playerHandler.getSnake().countPoints() == maxPoints) {
                    winner.add(playerHandler);
                }
            }
        } else if (playerController.getPlayerHandlerList().size() <= 1) {
            winner.add(playerController.getPlayerHandlerList().get(0));
        }

        return winner;
    }

    private void validateEvents(PlayerHandler playerHandler, PlayerChoice playerChoice) {
        List<RoundEvent> roundEvents = playerHandler.getRoundEvents();

        if (playerHandler.isDead() || playerHandler.getSnake().countPoints() <= 1) {
            roundEvents.add(DIED);
            playerHandler.kill();
            return;
        }

        if (!playerChoice.isHasChosen() || !playerHandler.isOrientationValid(playerChoice.getOrientation())) {
            roundEvents.add(CONFUSED);
            playerHandler.setConfused(true);
            return;
        }

        Point2D nextPosition = playerHandler.getNextPositionBy(playerChoice.getOrientation());
        if (level.checkCollision(nextPosition)) {
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
                    player.getRoundEvents().add(BIT_BY_PLAYER);
                }
                playerHandler.setGhostMode();
            }
        }

        if (!playerHandler.isGhostMode() && level.tryConsumePoint(nextPosition)) {
            roundEvents.add(CONSUMED_POINT);
        }
    }

    public List<PlayerHandler> getPlayerHandler() {
        return playerController.getPlayerHandlerList();
    }

    public void shutdown() {
        this.playerController.shutdown();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Level getLevel() {
        return level;
    }
}
