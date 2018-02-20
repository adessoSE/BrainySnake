package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import de.adesso.brainysnake.Config;

public abstract class AbstractScreen extends Stage implements Screen {

    protected Skin defaultSkin = new Skin();

    protected BitmapFont defaultFont = new BitmapFont();

    public AbstractScreen() {
        createDefaultSkin();
    }

    private void createDefaultSkin() {
        defaultSkin.add("default", defaultFont);

        Pixmap pixmap = new Pixmap(Config.APPLICATION_WIDTH / 10, Config.APPLICATION_HEIGHT / 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        defaultSkin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = defaultSkin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = defaultSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = defaultSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = defaultSkin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = defaultSkin.getFont("default");
        defaultSkin.add("default", textButtonStyle);
    }

    /**
     * Subclasses must load actors in this method
     */
    public abstract void initialize();

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Calling to Stage methods
        super.act(delta);
        super.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        super.dispose();
        defaultFont.dispose();
        defaultSkin.dispose();
    }
}
