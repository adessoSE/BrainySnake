package de.adesso.brainysnake.gamelogic;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.gamelogic.player.*;
import de.adesso.brainysnake.renderer.BrainySnake;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.gamelogic.level.GlobalGameState;
import de.adesso.brainysnake.gamelogic.level.LevelBoard;
import de.adesso.brainysnake.playercommon.Field;
import de.adesso.brainysnake.playercommon.FieldType;
import de.adesso.brainysnake.playercommon.PlayerView;
import de.adesso.brainysnake.playercommon.RoundEvent;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.level.LevelObject;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.adesso.brainysnake.playercommon.RoundEvent.*;

/**
 * Takes over the role of the game director. Knows the rules of the game and takes on professional validation, which concerns the game logic.
 */
public class GameMaster {

    private GameBoard gameBoard;

    private BrainySnake brainySnake;

    private PlayerController playerController;

    private LevelBoard levelBoard;

    public GameMaster(GameBoard gameBoard) {
        brainySnake = new BrainySnake();
        brainySnake.initialize();
        brainySnake.create();

        levelBoard = new LevelBoard(Config.LEVEL_WIDTH, Config.LEVEL_HEIGHT);

        this.gameBoard = gameBoard;
        GlobalGameState.initialize(gameBoard.getRemainingRoundsToPlay());
        playerController = new PlayerController(gameBoard.getBrainySnakePlayers(), levelBoard);
    }

    /**
     * Checks if one of the Players has one, otherwise calls the method gameLoop() and updateGame().
     * If one of the Players has one, gameOver() is called and update() simply returns.
     */

    public void update(float delta) {
        //check if game is over
        if (!checkIfPlayerWon().isEmpty()) {
            gameOver();
            return;
        }

        gameLoop();
        updateGame();
    }

    /**
     * The main gameLoop.
     * Updates the playerView, calculates the playerState und updates the playerController.
     * Although gameLoop handles Events and their effects on the game.
     * Furthermore it spreads new points once they have been consumed.
     */

    public void gameLoop() {

        GlobalGameState.increasePastRounds();

        //check if game is over
        if (!checkIfPlayerWon().isEmpty()) {
            gameOver();
            return;
        }

        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            //update view of player
            updateRoundForPlayer(playerHandler);

            // calculates the playerState and updates the playerController via call
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
                        playerHandler.kill();
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
                        collectedPoints++;
                        break;
                    case BIT_BY_PLAYER:
                        // agent was Hit by another agent
                        collectedPoints--;
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

        // spread new points in level
        levelBoard.fillUpWithPoints(Config.MAX_POINTS_IN_LEVEL);

        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            // reset data of player
            playerHandler.endRound();
        }

        GlobalGameState.increasePastRounds();

        //Update Metadata of game and player
        updateGameBaordData();
        playerController.removeDeadPlayer();
    }

    /**
     * Calls dispose in brainySnake and activates the GameOverScreen.
     */

    public void gameOver() {
        brainySnake.dispose();
        ScreenManager.getINSTANCE().showScreen(ScreenType.GAME_OVER_SCREEN);
    }

    /**
     * Updates the game and calls updateSnake, render in brainySnake.
     */

    public void updateGame() {
        brainySnake.updateLevelPoints(levelBoard.getPoints(), levelBoard.getBarriers(), levelBoard.getWalls(), levelBoard.getPointLabyrinths());

        List<LevelObject> snakes = new ArrayList<>();
        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            Snake snake = playerHandler.getSnake();

            if (Config.RENDER_PLAYERVIEW && playerHandler.getPlayerView() != null) {
                snakes.addAll(drawPlayerView(playerHandler.getPlayerView()));
            }
            snakes.add(snake.getBody());
            snakes.add(snake.getHead());
        }

        brainySnake.updateSnakes(snakes);
        brainySnake.render();
    }

    /**
     * Updates the meta information of the game and the player in the gameboard
     */
    private void updateGameBaordData() {
        //Update Playerboards
        for (PlayerHandler playerHandler : playerController.getPlayerHandlerList()) {
            playerHandler.updatePlayerBoard();
        }

        //Update Gameboard
        gameBoard.updateGameBoard(GlobalGameState.movesRemaining());
    }

    /**
     * Draws the playerView.
     */
    public List<LevelObject> drawPlayerView(PlayerView playerView) {
        List<LevelObject> levelObject = new ArrayList<>();

        for (Field field : playerView.getVisibleFields()) {
            LevelObject temp = new LevelObject();
            temp.getPositions().add(field.getPosition());
            switch (field.getFieldType()) {
                case EMPTY:
                    temp.setColor(Color.DARK_GRAY);
                    break;
                case LEVEL:
                    temp.setColor(Color.GREEN);
                    break;
                case PLAYER:
                    temp.setColor(Color.PINK);
                    break;
                case POINT:
                    temp.setColor(Color.ORANGE);
                    break;
                default:
                    temp.setColor(Color.PINK);
            }

            levelObject.add(temp);
        }

        return levelObject;
    }

    /**
     * Updates the PlayerView.
     */
    private void updateRoundForPlayer(PlayerHandler playerHandler) {
        List<Point2D> playerViewPositions = PlayerViewHelper.generatePlayerView(playerHandler.getCurrentOrientation(), playerHandler.getHeadPosition());
        List<Point2D> playerPositions = playerController.getPlayerPositions();
        List<Field> playerView = new ArrayList<>();
        for (Point2D point2D : playerViewPositions) {
            if (!levelBoard.contains(point2D)) {
                playerView.add(new Field(point2D, FieldType.NONE));
                continue;
            }

            if (levelBoard.checkCollision(point2D)) {
                playerView.add(new Field(point2D, FieldType.LEVEL));
            } else if (levelBoard.isPointOn(point2D)) {
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
     * Checks if a player has won the game. When the time is up, the player with the most points has won.
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

    /**
     * Validates Events and adds them to the roundEvents.
     */

    private void validateEvents(PlayerHandler playerHandler, PlayerChoice playerChoice) {
        List<RoundEvent> roundEvents = playerHandler.getRoundEvents();

        if (playerHandler.isDead() || playerHandler.getSnake().countPoints() <= 1) {
            roundEvents.add(DIED);
            return;
        }

        if (!playerChoice.isHasChosen() || !playerHandler.isOrientationValid(playerChoice.getOrientation())) {
            roundEvents.add(CONFUSED);
            playerHandler.setConfused(true);
            return;
        }

        Point2D nextPosition = playerHandler.getNextPositionBy(playerChoice.getOrientation());
        if (levelBoard.checkCollision(nextPosition)) {
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

        if (!playerHandler.isGhostMode() && levelBoard.tryConsumePoint(nextPosition)) {
            roundEvents.add(CONSUMED_POINT);
        }
    }
}
