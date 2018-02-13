package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerUpdate;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PlayerControllerTest {

    Level level;
    PlayerController playerController;
    //Alle Spieler erzeugen
    private BrainySnakePlayer playerOne = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer One";
        }
    };
    private BrainySnakePlayer playerTwo = new SamplePlayer() {
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
    private BrainySnakePlayer playerFour = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer Four";
        }
    };

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
        LinkedList<Snake> brainySnakePlayersUiModel = new LinkedList<>();
        brainySnakePlayersUiModel.add(level.createStartingGameObject(Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.add(level.createStartingGameObject(Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.add(level.createStartingGameObject(Config.INITIAL_PLAYER_LENGTH));
        brainySnakePlayersUiModel.add(level.createStartingGameObject(Config.INITIAL_PLAYER_LENGTH));

        // The PlayerController capsules agent actions an calculations
        // The Controller will randomly assign agents to GameObjects
        playerController = new PlayerController(brainySnakePlayers, snakes);
    }

    @Test
    public void pushPlayerState() throws Exception {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = playerHandlerFuture.get();
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

    @Test
    public void pushPlayerStateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = playerHandlerFuture.get(1, TimeUnit.MILLISECONDS);
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

    @Test
    public void requestPlayerUpdate() throws Exception {
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = playerController.requestPlayerUpdate();

        Future<PlayerUpdate> playerHandlerFuture;
        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            PlayerUpdate playerUpdate = playerHandlerFuture.get();
            System.out.println("Player: " + playerHandler.getPlayerName() + " updates " + playerUpdate.getNextStep());
        }
    }

    @Test
    public void requestPlayerUpdateTimeLimit() throws Exception {
        Map<PlayerHandler, Future<PlayerUpdate>> playerHandlerFutureMap = playerController.requestPlayerUpdate();

        Future<PlayerUpdate> playerHandlerFuture;
        for (PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            PlayerUpdate playerUpdate = playerHandlerFuture.get(1, TimeUnit.SECONDS);
            Assert.assertTrue(playerHandlerFuture.isDone());

            System.out.println("Player: " + playerHandler.getPlayerName() + " updates " + playerUpdate.getNextStep());
        }
    }


    @Test
    public void shutdown() throws Exception {
        //TODO
    }

}
