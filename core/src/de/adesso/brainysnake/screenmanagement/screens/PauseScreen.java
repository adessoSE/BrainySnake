package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

import java.awt.*;

public class PauseScreen extends AbstractScreen {

    private TextButton resumeButton;

    private TextButton exitGameButton;

    private GameScreen gameScreen;

    private Image logoPause;

    public PauseScreen(Screen screen) {
        super();
        gameScreen = (GameScreen) screen;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
        TextureRegion region = new TextureRegion(new Texture("logoPause.png"), 0, 0, 512, 512);
        logoPause = new Image(region);

        logoPause.setPosition(Config.APPLICATION_WIDTH / 3 - 50, Config.APPLICATION_HEIGHT / 4);
        addActor(logoPause);

        resumeButton = new TextButton("Resume", defaultSkin);
        resumeButton.setPosition(Config.APPLICATION_WIDTH / 2 - 375, Config.APPLICATION_HEIGHT / 2);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().togglePauseGame();
            }
        });
        addActor(resumeButton);

        exitGameButton = new TextButton("Exit", defaultSkin);
        exitGameButton.setPosition(Config.APPLICATION_WIDTH / 2 + 250, Config.APPLICATION_HEIGHT / 2);
        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().showScreen(ScreenType.EXIT_SCREEN);
            }
        });
        addActor(exitGameButton);
    }

    @Override
    public void render(float delta) {
        this.gameScreen.getBrainySnake().render();

        super.act(delta);
        super.draw();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
