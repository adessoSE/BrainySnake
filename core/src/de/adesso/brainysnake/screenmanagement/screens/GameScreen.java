package de.adesso.brainysnake.screenmanagement.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.gamelogic.GameBoard;
import de.adesso.brainysnake.gamelogic.GameMaster;
import de.adesso.brainysnake.gamelogic.PlayerBoard;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

import java.util.List;

public class GameScreen extends AbstractScreen {

    private GameMaster gameMaster;
    private TextButton resumeButton;
    private TextButton exitGameButton;
    private Image logoPause;
    private boolean isGamePaused;

    public GameScreen() {
        gameMaster = new GameMaster(GameBoard.getINSTANCE());
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
        initializePauseScreen();
    }

    private void initializePauseScreen() {
        TextureRegion region = new TextureRegion(new Texture("logoPause.png"), 0, 0, 512, 512);
        logoPause = new Image(region);

        logoPause.setPosition(Config.APPLICATION_WIDTH / 3 - 50, Config.APPLICATION_HEIGHT / 4);

        resumeButton = new TextButton("Resume", defaultSkin);
        resumeButton.setPosition(Config.APPLICATION_WIDTH / 2 - 375, Config.APPLICATION_HEIGHT / 2);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isGamePaused = false;
                showPauseScreen(isGamePaused);
            }
        });

        exitGameButton = new TextButton("Game Over", defaultSkin);
        exitGameButton.setPosition(Config.APPLICATION_WIDTH / 2 + 250, Config.APPLICATION_HEIGHT / 2);
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameMaster.gameOver();
            }
        });

        logoPause.setVisible(isGamePaused);
        resumeButton.setVisible(isGamePaused);
        exitGameButton.setVisible(isGamePaused);

        addActor(logoPause);
        addActor(resumeButton);
        addActor(exitGameButton);
    }

    @Override
    public void render(float delta) {

        //Show pause screen
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isGamePaused = !isGamePaused;
            showPauseScreen(isGamePaused);
        }

        if (!isGamePaused) {
            gameMaster.update(delta);
        }
        drawGameUI();

        super.act(delta);
        super.draw();
    }

    private void showPauseScreen(boolean show) {
        logoPause.setVisible(show);
        resumeButton.setVisible(show);
        exitGameButton.setVisible(show);
    }

    /**
     * Draw the game ui.
     * Contains the player names including points reached so far
     */
    private void drawGameUI() {
        getBatch().begin();
        defaultFont.getData().setScale(1f);

        defaultFont.setColor(Color.WHITE);
        defaultFont.draw(getBatch(), "Rounds remaining: ", 20, Config.APPLICATION_HEIGHT - 20);
        defaultFont.draw(getBatch(), GameBoard.getINSTANCE().getRemainingRoundsToPlay() + "", 165, Config.APPLICATION_HEIGHT - 20);

        List<PlayerBoard> playerBoards = GameBoard.getINSTANCE().getPlayerBoards();
        int offset = 20;
        for (PlayerBoard playerBoard : playerBoards) {
            defaultFont.setColor(playerBoard.getColor());
            defaultFont.draw(getBatch(), playerBoard.getName(), 20, Config.APPLICATION_HEIGHT - 20 - offset);
            defaultFont.draw(getBatch(), playerBoard.getPoints() + "", 165, Config.APPLICATION_HEIGHT - 20 - offset);
            offset += 20;
        }

        getBatch().end();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
