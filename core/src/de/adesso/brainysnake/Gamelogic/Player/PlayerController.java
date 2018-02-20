package de.adesso.brainysnake.Gamelogic.Player;

import java.util.*;
import java.util.concurrent.*;

import com.badlogic.gdx.graphics.Color;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Utils;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
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

    private PlayerStatePushExecutorService playerStatePushExecutorService;

    private PlayerUpdateGetExecutorService playerUpdateGetExecutorService;

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

        // create Thread handlers
        this.playerStatePushExecutorService = new PlayerStatePushExecutorService(this.playerHandlerList, Config.MAX_AGENT_PROCESSING_TIME_MS);
        this.playerUpdateGetExecutorService = new PlayerUpdateGetExecutorService(this.playerHandlerList, Config.MAX_AGENT_PROCESSING_TIME_MS);
    }

    public void updatePlayerState(GlobalGameState gameState) {

        for (PlayerHandler player : this.playerHandlerList) {
            player.calculatePlayerState(gameState);
        }

        this.playerStatePushExecutorService.process();
    }


    /**
     * Returns the choice for every agent
     *
     * @return
     */
    public Map<PlayerHandler, PlayerChoice> getPlayerStatus() {
        Map<PlayerHandler, PlayerChoice> agentChoiceMap = new HashMap<PlayerHandler, PlayerChoice>();

        Map<PlayerHandler, Optional<PlayerUpdate>> updates = this.playerUpdateGetExecutorService.process();

        updates.forEach((playerHandler, playerUpdate) -> {
            agentChoiceMap.put(playerHandler, handlePlayerUpdate(playerUpdate, playerHandler));
        });

        return agentChoiceMap;
    }

    public void shutdown() {
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue


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

    private PlayerChoice handlePlayerUpdate(Optional<PlayerUpdate> playerUpdate, PlayerHandler playerHandler) {
        if(!playerUpdate.isPresent() || playerUpdate.get().getNextStep() == null) {
            LOGGER.error("PlayerController", "Player: " + playerHandler.getPlayerName() + " returns invalid PlayerUpdate");
            return PlayerChoice.createNoChoice();
        }
        return new PlayerChoice(playerUpdate.get().getNextStep());
    }

    public LinkedList<Point2D> getPlayerPositions() {
        LinkedList<Point2D> playerPositions = new LinkedList<Point2D>();
        for (PlayerHandler playerHandler : playerHandlerList) {
            playerPositions.addAll(playerHandler.getSnake().getAllSnakePositions());
        }

        return playerPositions;
    }
}
