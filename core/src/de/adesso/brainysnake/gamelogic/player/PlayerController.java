package de.adesso.brainysnake.gamelogic.player;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.gamelogic.level.LevelBoard;
import de.adesso.brainysnake.gamelogic.PlayerBoard;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Encapsulates thread- calls to the agents
 */
public class PlayerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class.getName());

    private ArrayList<PlayerHandler> playerHandlerList = new ArrayList<>();

    private PlayerStatePushExecutorService playerStatePushExecutorService;

    private PlayerUpdateGetExecutorService playerUpdateGetExecutorService;

    public PlayerController(Map<Long, PlayerBoard> playerMap, LevelBoard levelBoard) {

        // Add player to handler
        for (PlayerBoard player : playerMap.values()) {
            Snake newPlayerSnake = levelBoard.createStartingGameObject(Config.INITIAL_PLAYER_LENGTH, player.getColor());
            playerHandlerList.add(new PlayerHandler(player, newPlayerSnake));
        }

        // create Thread handlers
        playerStatePushExecutorService = new PlayerStatePushExecutorService(playerHandlerList, Config.MAX_AGENT_PROCESSING_TIME_MS);
        playerUpdateGetExecutorService = new PlayerUpdateGetExecutorService(playerHandlerList, Config.MAX_AGENT_PROCESSING_TIME_MS);
    }

    public void updatePlayerState() {

        for (PlayerHandler player : this.playerHandlerList) {
            player.calculatePlayerState();
        }

        playerStatePushExecutorService.process();
    }

    /**
     * Returns the choice for every agent
     */
    public Map<PlayerHandler, PlayerChoice> getPlayerStatus() {
        Map<PlayerHandler, PlayerChoice> agentChoiceMap = new HashMap<>();
        Map<PlayerHandler, Optional<PlayerUpdate>> updates = playerUpdateGetExecutorService.process();

        updates.forEach((playerHandler, playerUpdate) -> {
            agentChoiceMap.put(playerHandler, handlePlayerUpdate(playerUpdate, playerHandler));
        });

        return agentChoiceMap;
    }

    public List<PlayerHandler> getPlayerHandlerList() {
        return playerHandlerList;
    }

    private PlayerChoice handlePlayerUpdate(Optional<PlayerUpdate> playerUpdate, PlayerHandler playerHandler) {
        if (!playerUpdate.isPresent() || playerUpdate.get().getNextStep() == null) {
            LOGGER.error("player: {} returns invalid PlayerUpdate", playerHandler.getPlayerIdentifier());
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

    /**
     * Removes dead player from playerHandlerList
     */
    public void removeDeadPlayer() {
        // remove dead player from player list
        List<PlayerHandler> deadPlayers = new ArrayList<>();
        for (PlayerHandler playerHandler : playerHandlerList) {
            if (playerHandler.isDead()) {
                deadPlayers.add(playerHandler);
            }
        }

        for (PlayerHandler deadPlayer : deadPlayers) {
            playerHandlerList.remove(deadPlayer);
        }
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
}
