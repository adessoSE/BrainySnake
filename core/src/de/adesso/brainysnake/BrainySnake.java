package de.adesso.brainysnake;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Game;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.Gamelogic.UI.UiState;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class BrainySnake extends ApplicationAdapter {

    private Texture texture;
    private SpriteBatch gameSpriteBatch;
    private SpriteBatch fontSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;

    private static int DOT_SITZE = 10;
    private static int WIDTH = Config.APPLICATION_WIDTH / DOT_SITZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / DOT_SITZE;
    private static int APPLICATION_WIDTH = Config.APPLICATION_WIDTH;
    private static int APPLICATION_HEIGHT = Config.APPLICATION_HEIGHT;

    private OrthographicCamera mainCamera;
    private OrthographicCamera fontCamera;

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private float timeSinceLastRender = 0;

    public int renderCount = 0;
    private BitmapFont font;

    List<GameObject> gameObjects;

    private Game game;

    @Override
    public void create() {
        pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);

        texture = new Texture(pixmap);

        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        sprite = new Sprite(texture);

        initializeCamera();

        game = new Game();
        game.init(HEIGHT, WIDTH);
        Gdx.input.setInputProcessor(new KeyBoardControl(game));

        gameSpriteBatch = new SpriteBatch();
        gameSpriteBatch.setProjectionMatrix(mainCamera.combined);

        fontSpriteBatch = new SpriteBatch();
        fontSpriteBatch.setProjectionMatrix(fontCamera.combined);
        font = new BitmapFont();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeSinceLastRender += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
            // Do the actual rendering, pass timeSinceLastRender as delta time.
            timeSinceLastRender = 0f;
            game.update(Gdx.graphics.getDeltaTime());
        }
        draw();
    }

    private void initializeCamera() {
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, WIDTH, HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, APPLICATION_WIDTH , APPLICATION_HEIGHT);
    }

    public void draw() {
        // Redraw the head.
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        gameObjects = game.draw(Gdx.graphics.getDeltaTime());
        for (GameObject gameObject : gameObjects) {
            pixmap.setColor(gameObject.getColor());
            for (Point2D tempDot : gameObject.getPositions()) {
                pixmap.drawPixel(tempDot.getX(), tempDot.getY());
            }
        }
        gameObjects.clear();

        // draw pixmap to gameSpriteBatch
        texture.draw(pixmap, 0, 0);
        gameSpriteBatch.begin();
        sprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();

        fontSpriteBatch.begin();
        font.getData().setScale(1f);
        HashMap<String, Color> playerMap = UiState.getINSTANCE().getPlayerMap();
        int offset = 0;
        for (String playername : playerMap.keySet()) {
            font.setColor(playerMap.get(playername));
            font.draw(fontSpriteBatch, playername, 20, APPLICATION_HEIGHT - 20 - offset);
            offset += 20;
        }

        fontSpriteBatch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
        gameSpriteBatch.dispose();
    }

}
