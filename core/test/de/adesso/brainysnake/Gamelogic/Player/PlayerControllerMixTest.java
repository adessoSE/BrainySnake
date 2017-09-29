package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.TestPlayer.BlockingPlayer;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static de.adesso.brainysnake.playercommon.Orientation.*;
import static de.adesso.brainysnake.playercommon.Orientation.LEFT;

public class PlayerControllerMixTest {


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
    BrainySnakePlayer playerThree = new BlockingPlayer();
    private BrainySnakePlayer playerFour  = new SamplePlayer() {
        @Override
        public String getPlayerName() {
            return "SamplePlayer Four";
        }
    };


    Level level;
    List<PlayerHandler> playerHandlerList = new ArrayList<PlayerHandler>();
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
    public void pushPlayerStateTimeLimit(){
        Map<PlayerHandler, Future<Boolean>> playerHandlerFutureMap = playerController.pushPlayerState(new GlobalGameState());

        Future<Boolean> playerHandlerFuture;
        for(PlayerHandler playerHandler : playerHandlerFutureMap.keySet()) {
            playerHandlerFuture = playerHandlerFutureMap.get(playerHandler);

            Boolean aBoolean = null;
            try {
                aBoolean = playerHandlerFuture.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            System.out.println("Player: " + playerHandler.getPlayerName() + " accepted " + aBoolean);
        }
    }

    @Test
    public void pushPlayerStateTimeLimit2(){
        Map<PlayerHandler, PlayerChoice> playerStatus = this.playerController.getPlayerStatus();
        for(PlayerHandler playerHandler: playerStatus.keySet()) {
            PlayerChoice playerChoice = playerStatus.get(playerHandler);
            System.out.println("Name " + playerHandler.getPlayerName() + " " + playerChoice.getOrientation());
        }
    }
}
