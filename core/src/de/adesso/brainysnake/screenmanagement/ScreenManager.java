package de.adesso.brainysnake.screenmanagement;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import de.adesso.brainysnake.*;
import de.adesso.brainysnake.Gamelogic.GameBoard;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.PlayerBoard;
import de.adesso.brainysnake.screenmanagement.screens.*;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;

/**
 * Manage the current shown Screens in Game
 */
public class ScreenManager {

    private static ScreenManager INSTANCE = new ScreenManager();

    private Game game;

    private boolean gamePaused = false;

    private ScreenManager() {

    }

    public static ScreenManager getINSTANCE() {
        return INSTANCE;
    }

    // Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
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
                MatchPreviewScreen matchScreen = new MatchPreviewScreen();
                matchScreen.initialize();
                setGameScreen(matchScreen);
                break;

            case GAME_SCREEN:
                GameScreen gameScreen = new GameScreen();
                gameScreen.initialize();
                setGameScreen(gameScreen);
                break;

            case GAME_OVER_SCREEN:
                GameOverScreen gameOverScreen = new GameOverScreen();
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
