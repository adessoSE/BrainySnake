package de.adesso.brainysnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.GameController;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.Gamelogic.UI.UIPlayerInformation;
import de.adesso.brainysnake.Gamelogic.UI.UiState;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class BrainySnake extends ApplicationAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrainySnake.class.getName());
    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private static int DOT_SITZE = Config.DOT_SIZE;
    private static int WIDTH = Config.APPLICATION_WIDTH / DOT_SITZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / DOT_SITZE;
    private static int APPLICATION_WIDTH = Config.APPLICATION_WIDTH;
    private static int APPLICATION_HEIGHT = Config.APPLICATION_HEIGHT;
    private Texture texture;
    private SpriteBatch gameSpriteBatch;
    private SpriteBatch fontSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;
    private Music backgroundSound;
    private OrthographicCamera mainCamera;
    private OrthographicCamera fontCamera;
    private float timeSinceLastRender = 0;

    private BitmapFont font = new BitmapFont();
    private List<GameObject> gameObjects;
    private GameController gameController = new GameController();

    public BrainySnake(GameMaster gameMaster){
        gameController.init(gameMaster);
    }

    @Override
    public void create() {

        pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);

        texture = new Texture(pixmap);
        sprite = new Sprite(texture);

        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        backgroundSound = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
        startPlayingMusic(backgroundSound);

        initializeCamera();

        Gdx.input.setInputProcessor(new KeyBoardControl());

        gameSpriteBatch = new SpriteBatch();
        gameSpriteBatch.setProjectionMatrix(mainCamera.combined);

        fontSpriteBatch = new SpriteBatch();
        fontSpriteBatch.setProjectionMatrix(fontCamera.combined);
    }

    /**
     * Drawing the StartScreen. Contains the button "New Game" and "Exit" and the logo of BrainySnake.
     */
    private void startPlayingMusic(Music sound) {
        if (!sound.isPlaying()) {
            sound.setLooping(true);
            sound.play();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeSinceLastRender += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
            // Do the actual rendering, pass timeSinceLastRender as delta time.
            timeSinceLastRender = 0f;
            gameController.update(Gdx.graphics.getDeltaTime());
        }

        drawGameLoop();
    }

    private void initializeCamera() {

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, WIDTH, HEIGHT);

        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, APPLICATION_WIDTH, APPLICATION_HEIGHT);
    }

    public void drawGameLoop() {
        // Redraw the head.
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        gameObjects = gameController.draw(Gdx.graphics.getDeltaTime());
        for (GameObject gameObject : gameObjects) {
            pixmap.setColor(gameObject.getColor());
            for (Point2D tempDot : gameObject.getPositions()) {
                pixmap.drawPixel(tempDot.getX(), tempDot.getY());
            }
        }
        gameObjects.clear();

        // drawGameLoop pixmap to gameSpriteBatch
        texture.draw(pixmap, 0, 0);
        gameSpriteBatch.begin();
        sprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();


        fontSpriteBatch.begin();
        font.getData().setScale(1f);

        font.setColor(Color.WHITE);
        font.draw(fontSpriteBatch, "Rounds remaining: ", 20, APPLICATION_HEIGHT - 20);
        font.draw(fontSpriteBatch, UiState.getINSTANCE().getRoundsRemaining(), 165, APPLICATION_HEIGHT - 20);

        HashMap<String, UIPlayerInformation> playerMap = UiState.getINSTANCE().getPlayerMap();
        int offset = 20;
        for (String playername : playerMap.keySet()) {
            font.setColor(playerMap.get(playername).getColor());
            font.draw(fontSpriteBatch, playername, 20, APPLICATION_HEIGHT - 20 - offset);
            font.draw(fontSpriteBatch, playerMap.get(playername).getPoints(), 165, APPLICATION_HEIGHT - 20 - offset);
            offset += 20;
        }

        fontSpriteBatch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        gameSpriteBatch.dispose();
        pixmap.dispose();
        backgroundSound.dispose();
    }
}
