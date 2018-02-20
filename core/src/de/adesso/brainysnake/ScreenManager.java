package de.adesso.brainysnake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.GameController;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.Level.Level;

//TODO rukl doc
public class ScreenManager {

    private static ScreenManager INSTANCE = new ScreenManager();
    private static int WIDTH = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;

    private Game game;

    private GameMaster gameMaster;

    private ScreenManager() {

    }

    public static ScreenManager getINSTANCE() {
        return INSTANCE;
    }

    // Initialization with the game class
    public void initialize(Game game) {
        this.game = game;
    }

    public Screen showScreen(ScreenType screenType) {
        // Get current screen to dispose it
        Screen currentScreen = game.getScreen();

        // Show new screen
        Screen newScreen = null;
        switch (screenType) {
            case MAIN_MENU:
                MainMenuScreen mainMenuScreen = new MainMenuScreen();
                mainMenuScreen.initialize();
                setGameScreen(mainMenuScreen);
                break;

            case MATCHPREVIEW_SCREEN:
                if (gameMaster == null) {
                    throw new IllegalStateException("GameMaster is not initialized");
                }

                MatchPreviewScreen matchScreen = new MatchPreviewScreen(gameMaster.getPlayerHandler());
                matchScreen.initialize();
                setGameScreen(matchScreen);
                break;

            case GAME_SCREEN:
                if (gameMaster == null) {
                    throw new IllegalStateException("GameMaster is not initialized");
                }

                GameScreen gameScreen = new GameScreen(gameMaster);
                gameScreen.initialize();
                setGameScreen(gameScreen);
                break;

            case WINNER_SCREEN:
                if (gameMaster == null) {
                    throw new IllegalStateException("GameMaster is not initialized");
                }

                GameOverScreen gameOverScreen = new GameOverScreen(gameMaster.getPlayerHandler(), gameMaster.getDeadPlayer());
                gameOverScreen.initialize();
                setGameScreen(gameOverScreen);
                break;

            case EXIT_SCREEN:
                Gdx.app.exit();
                break;

            default:
                break;
        }

        // Dispose previous screen
        if (currentScreen != null) {
            currentScreen.dispose();
        }

        return newScreen;
    }

    private void preLoadGame() {
        gameMaster = new GameMaster(new Level(HEIGHT, WIDTH, Color.WHITE));
    }

    public void startGame() {
        preLoadGame();
        showScreen(ScreenType.GAME_SCREEN);
    }

    public void restartGame() {
        showScreen(ScreenType.GAME_SCREEN);
    }

    private void setGameScreen(Screen screen) {
        game.setScreen(screen);
    }

}
