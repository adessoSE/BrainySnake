package de.adesso.brainysnake.Gamelogic.Player;

import java.util.*;
import java.util.concurrent.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Utils;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Encapsulates thread- calls to the agents
 */
public class PlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class.getName());

    private ArrayList<PlayerHandler> playerHandlerList = new ArrayList<PlayerHandler>();

    private ExecutorService playerStatePushExecutor;

    private ExecutorService playerUpdateCallableExecutor;

    public PlayerController(List<BrainySnakePlayer> playerList, LinkedList<Snake> playerGameObjects) {

        // Shuffle Player Colors
        List<Color> playerColors = Utils.getShuffledGameColors();

        if (playerList.size() > playerColors.size() || playerList.size() > playerGameObjects.size()) {
            throw new IllegalArgumentException("Too many players");
        }

        // Shuffle Starting Positions
        LinkedList<Snake> snakeList = new LinkedList<>(playerGameObjects);

        // Add player to handler
        for (BrainySnakePlayer player : playerList) {

            // Build the PlayerHandler
            Color color = playerColors.remove(playerColors.size() - 1);
            Snake currentSnake = snakeList.removeFirst();
            currentSnake.setColor(color);

            playerHandlerList.add(new PlayerHandler(player, currentSnake.getStartOrientation(), currentSnake));
        }

        // We need a Thread for every player
        playerStatePushExecutor = Executors.newFixedThreadPool(this.playerHandlerList.size());
        playerUpdateCallableExecutor = Executors.newFixedThreadPool(this.playerHandlerList.size());
    }

    public void updatePlayerState(GlobalGameState gameState) {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = this.pushPlayerState(gameState);

        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            Future<Boolean> hasProcessedFuture = playerHandlerFutureMap.get(playerHandler);
            try {
                hasProcessedFuture.get(Config.MAX_AGENT_PROCESSING_TIME_MS, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("Player: {} got Timeout after  {} ms {}", playerHandler.getPlayerName(), Config.MAX_AGENT_PROCESSING_TIME_MS, e );
                        e.printStackTrace();
            } catch (ExecutionException e) {
                LOGGER.error("ExecutionException {}" + e);
                e.printStackTrace();
            } catch (TimeoutException e) {
                LOGGER.error("Waiting for Player {} aborted cause of Timeout", playerHandler.getPlayerName());
            }
        }
    }

    Map<PlayerHandler, Future<Boolean>> pushPlayerState(GlobalGameState gameState) {
        Map<PlayerHandler, Future<Boolean>> playerPushes = new HashMap<PlayerHandler, Future<Boolean>>();

        for (PlayerHandler player : this.playerHandlerList) {
            player.calculatePlayerState(gameState);

            Callable pushWorker = new PlayerStatePush(player);
            Future pushProcessed = playerStatePushExecutor.submit(pushWorker);

            playerPushes.put(player, pushProcessed);
        }
        return playerPushes;
    }

    /**
     * Returns the choice for every agent
     *
     * @return
     */
    public Map<PlayerHandler, PlayerChoice> getPlayerStatus() {
        Map<PlayerHandler, PlayerChoice> agentChoiceMap = new HashMap<PlayerHandler, PlayerChoice>();

        // Updates from agents
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = this.requestPlayerUpdate();

        // Players choice
        PlayerUpdate playerUpdate;

        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {

            Future<PlayerUpdate> playerUpdateFuture = playerHandlerFutureMap.get(playerHandler);
            try {
                playerUpdate = playerUpdateFuture.get(Config.MAX_AGENT_PROCESSING_TIME_MS, TimeUnit.MILLISECONDS);
                agentChoiceMap.put(playerHandler, handlePlayerUpdate(playerUpdate, playerHandler));
            } catch (InterruptedException e) {
                LOGGER.error("PlayerController: Future Operation was interrupted ", e.getMessage());
            } catch (ExecutionException e) {
                LOGGER.error("PlayerController: ExecutionException ", e.getMessage());
                e.printStackTrace();
            } catch (TimeoutException e) {
                agentChoiceMap.put(playerHandler, PlayerChoice.createNoChoice());
                playerHandler.kill();
                LOGGER.error("PlayerController",
                        "Player: " + playerHandler.getPlayerName() + " got Timeout after " + Config.MAX_AGENT_PROCESSING_TIME_MS + " ms and got killed ", e);
                continue;
            }

        }
        return agentChoiceMap;
    }

    Map<PlayerHandler, Future<PlayerUpdate>> requestPlayerUpdate() {
        Map<PlayerHandler, Future<PlayerUpdate>> playerUpdates = new HashMap<PlayerHandler, Future<PlayerUpdate>>();

        for (PlayerHandler player : this.playerHandlerList) {
            Callable<PlayerUpdate> request = new PlayerUpdateRequestCallable(player);
            Future<PlayerUpdate> updateFuture = playerUpdateCallableExecutor.submit(request);
            playerUpdates.put(player, updateFuture);
        }
        return playerUpdates;
    }

    public void shutdown() {
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        playerStatePushExecutor.shutdownNow();
        playerUpdateCallableExecutor.shutdownNow();

        // Wait until all threads are finish
        // playerStatePushExecutor.awaitTermination();
    }

    public List<PlayerHandler> getPlayerHandlerList() {
        return playerHandlerList;
    }

    class PlayerStatePush implements Callable<Boolean> {

        private PlayerHandler playerHandler;

        public PlayerStatePush(PlayerHandler playerHandler) {
            this.playerHandler = playerHandler;
        }

        @Override
        public Boolean call() throws Exception {
            return playerHandler.sendPlayerState();
        }

    }

    class PlayerUpdateRequestCallable implements Callable<PlayerUpdate> {

        private PlayerHandler playerHandler;

        public PlayerUpdateRequestCallable(PlayerHandler playerHandler) {
            this.playerHandler = playerHandler;
        }

        @Override
        public PlayerUpdate call() throws Exception {
            return playerHandler.requestPlayerUpdate();
        }

    }

    private PlayerChoice handlePlayerUpdate(PlayerUpdate playerUpdate, PlayerHandler playerHandler) {
        if (playerUpdate == null || playerUpdate.getNextStep() == null) {
            LOGGER.error("PlayerController", "Player: " + playerHandler.getPlayerName() + " returns invalid PlayerUpdate");
            return PlayerChoice.createNoChoice();
        }
        return new PlayerChoice(playerUpdate.getNextStep());
    }

    public LinkedList<Point2D> getPlayerPositions() {
        LinkedList<Point2D> playerPositions = new LinkedList<Point2D>();
        for (PlayerHandler playerHandler : playerHandlerList) {
            playerPositions.addAll(playerHandler.getSnake().getAllSnakePositions());
        }

        return playerPositions;
    }
}
