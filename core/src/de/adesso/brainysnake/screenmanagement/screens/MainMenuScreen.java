package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

public class MainMenuScreen extends AbstractScreen {

    private final int BUTTON_OFFSET = 25;

    private TextButton startGameButton;

    private TextButton exitGameButton;

    private Image logoBrainySnake;

    public MainMenuScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
        TextureRegion region = new TextureRegion(new Texture("BrainySnake_Headline.png"), 0, 0, 629, 180);
        logoBrainySnake = new Image(region);

        logoBrainySnake.setPosition(Config.APPLICATION_WIDTH / 4, Config.APPLICATION_HEIGHT - 250);
        addActor(logoBrainySnake);
        logoBrainySnake.setVisible(true);

        startGameButton = new TextButton("New Game", defaultSkin);
        startGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().showScreen(ScreenType.MATCHPREVIEW_SCREEN);
            }
        });
        addActor(startGameButton);

        exitGameButton = new TextButton("Exit", defaultSkin);
        exitGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2 - 2 * (startGameButton.getHeight() + BUTTON_OFFSET));
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
        super.render(delta);
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
