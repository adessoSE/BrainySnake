package de.adesso.brainysnake.screenmanagement;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.*;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.screenmanagement.screens.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Manage the current shown Screens in Game
 */
public class ScreenManager {

    private static ScreenManager INSTANCE = new ScreenManager();
    private static int WIDTH = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;

    private Game game;

    private GameMaster gameMaster;

    private ArrayList<PlayerDTO> playerDTOS;

    private ArrayList<PlayerDTO> deadPlayerDTOS;

    private boolean gamePaused = false;

    private ScreenManager() {

    }

    public static ScreenManager getINSTANCE() {
        return INSTANCE;
    }

    // Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
        gameMaster = new GameMaster(new Level(HEIGHT, WIDTH, Color.WHITE));
    }

    public void showScreen(ScreenType screenType) {
        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        switch (screenType) {
            case MAIN_MENU:
                MainMenuScreen mainMenuScreen = new MainMenuScreen();
                mainMenuScreen.initialize();
                setGameScreen(mainMenuScreen);
                break;

            case MATCHPREVIEW_SCREEN:
                MatchPreviewScreen matchScreen = new MatchPreviewScreen(gameMaster.getPlayerHandler());
                matchScreen.initialize();
                setGameScreen(matchScreen);
                break;

            case GAME_SCREEN:
                GameScreen gameScreen = new GameScreen(gameMaster);
                gameScreen.initialize();
                setGameScreen(gameScreen);
                break;

            case WINNER_SCREEN:
                GameOverScreen gameOverScreen = new GameOverScreen(playerDTOS, deadPlayerDTOS);
                gameOverScreen.initialize();
                setGameScreen(gameOverScreen);
                break;

            case EXIT_SCREEN:
                Gdx.app.exit();
                break;

            case PAUSE_SCREEN:
                PauseScreen pauseScreen = new PauseScreen(currentScreen);
                pauseScreen.initialize();
                setGameScreen(pauseScreen);
                break;

            default:
                break;
        }

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }

    }

    public void finishGame(ArrayList<PlayerDTO>  player, ArrayList<PlayerDTO>  deadPlayer){
        this.playerDTOS = player;
        this.deadPlayerDTOS = deadPlayer;
        showScreen(ScreenType.WINNER_SCREEN);
    }

    private void setGameScreen(Screen screen) {
        game.setScreen(screen);
    }

    public void togglePauseGame() {
        this.gamePaused = !gamePaused;
        if(gamePaused){
            showScreen(ScreenType.PAUSE_SCREEN);
        } else showScreen(ScreenType.GAME_SCREEN);
    }

    public boolean isGamePaused() {
        return gamePaused;
    }
}
