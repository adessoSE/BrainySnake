package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.adesso.brainysnake.playercommon.Orientation.*;

public class PlayerControllerFailTest {

    SlowPlayer playerOne = new SlowPlayer();
    Level level;
    List<PlayerHandler> playerHandlerList = new ArrayList<PlayerHandler>();
    PlayerController playerController;


    @Before
    public void init() {
        level = new Level(100, 100, Color.WHITE);
        //Add agents to the game
        List<BrainySnakePlayer> brainySnakePlayers = new ArrayList<BrainySnakePlayer>();
        brainySnakePlayers.add(playerOne);

        // Build UI Models for the agents
        Map<Orientation, Snake> brainySnakePlayersUiModel = new HashMap<Orientation, Snake>();
        brainySnakePlayersUiModel.put(UP, level.createStartingGameObject(UP, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(DOWN, level.createStartingGameObject(DOWN, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(RIGHT,level.createStartingGameObject(RIGHT, Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.put(LEFT,level.createStartingGameObject(LEFT, Config.INITIAL_PLAYER_LENGTH));

        // The PlayerController capsules agent actions an calculations
        // The Controller will randomly assign agents to GameObjects
        playerController = new PlayerController(brainySnakePlayers, brainySnakePlayersUiModel);
    }

    @Test
    public void pushPlayerState() throws Exception {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = playerHandlerFuture.get();
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

    @Test(expected = TimeoutException.class)
    public void pushPlayerStateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = playerHandlerFuture.get(1, TimeUnit.MILLISECONDS);
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

    /**
     * This should work, because the Main-thread waits
     * @throws Exception
     */
    @Test
    public void requestPlayerUpdate() throws Exception {
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = playerController.requestPlayerUpdate();

        Future<PlayerUpdate> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            PlayerUpdate playerUpdate = playerHandlerFuture.get();
            System.out.println("Player: " + playerHandler.getPlayerName() + " updates " + playerUpdate.getNextStep());
        }
    }

    /**
     * This should not work, because the Main-thread waits
     * @throws Exception
     */
    @Test(expected = TimeoutException.class)
    public void requestPlayerUpdateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = playerController.requestPlayerUpdate();

        Future<PlayerUpdate> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            PlayerUpdate playerUpdate = playerHandlerFuture.get(1, TimeUnit.MILLISECONDS);
            Assert.assertFalse(playerHandlerFuture.isDone());

            System.out.println("Player: " + playerHandler.getPlayerName() + " updates " + playerUpdate.getNextStep());
        }
    }



    @Test
    public void shutdown() throws Exception {
    }

}
