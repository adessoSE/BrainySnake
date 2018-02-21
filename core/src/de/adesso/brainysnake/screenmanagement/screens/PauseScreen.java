package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

public class PauseScreen extends AbstractScreen {

    private final int BUTTON_OFFSET = 25;

    private TextButton resumeButton;

    private TextButton exitGameButton;

    public PauseScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
        resumeButton = new TextButton("Resume", defaultSkin);
        resumeButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().resumeGame();
            }
        });
        addActor(resumeButton);

        exitGameButton = new TextButton("Exit", defaultSkin);
        exitGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2 - 2 * (resumeButton.getHeight() + BUTTON_OFFSET));
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
        super.dispose();
    }
}
