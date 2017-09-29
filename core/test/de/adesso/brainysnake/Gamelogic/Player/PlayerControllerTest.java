package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerController;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static de.adesso.brainysnake.playercommon.Orientation.*;
import static de.adesso.brainysnake.playercommon.Orientation.LEFT;

public class PlayerControllerTest {

    //Alle Spieler erzeugen
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

    Level level;
    PlayerController playerController;


    @Before
    public void init() {
        level = new Level(100, 100, Color.WHITE);
        //Add agents to the game
        List<BrainySnakePlayer> brainySnakePlayers = new ArrayList<BrainySnakePlayer>();
        brainySnakePlayers.add(playerOne);
        brainySnakePlayers.add(playerTwo);
        brainySnakePlayers.add(playerThree);
        brainySnakePlayers.add(playerFour);

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

    @Test
    public void pushPlayerStateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = playerHandlerFuture.get(1, TimeUnit.MILLISECONDS);
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

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

    @Test
    public void requestPlayerUpdateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = playerController.requestPlayerUpdate();

        Future<PlayerUpdate> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            PlayerUpdate playerUpdate = playerHandlerFuture.get(1, TimeUnit.SECONDS);
            Assert.assertTrue(playerHandlerFuture.isDone());

            System.out.println("Player: " + playerHandler.getPlayerName() + " updates " + playerUpdate.getNextStep());
        }
    }



    @Test
    public void shutdown() throws Exception {
    }

}
