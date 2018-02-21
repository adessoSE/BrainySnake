package de.adesso.brainysnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.GameController;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BrainySnake extends ApplicationAdapter {

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private static int WIDTH = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;

    private Texture texture;
    private SpriteBatch gameSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;
    private Music backgroundSound;
    private OrthographicCamera mainCamera;
    private float timeSinceLastRender = 0;

    private List<GameObject> gameObjects;
    private GameController gameController = new GameController();

    public BrainySnake(){
        gameController.init(new GameMaster(new Level(HEIGHT, WIDTH)));
    }

    @Override
    public void create() {

        pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);

        texture = new Texture(pixmap);
        sprite = new Sprite(texture);

        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        backgroundSound = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
     //   startPlayingMusic(backgroundSound);

        initializeCamera();

        gameSpriteBatch = new SpriteBatch();
        gameSpriteBatch.setProjectionMatrix(mainCamera.combined);
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

    private void checkPressedKeys(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getINSTANCE().togglePauseGame();
        }
    }

    @Override
    public void render() {
        checkPressedKeys();

        if(!ScreenManager.getINSTANCE().isGamePaused()){
            timeSinceLastRender += Gdx.graphics.getDeltaTime();
            if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
                // Do the actual rendering, pass timeSinceLastRender as delta time.
                timeSinceLastRender = 0f;
                gameController.update(Gdx.graphics.getDeltaTime());
            }
        }
        drawGameLoop();
    }

    private void initializeCamera() {

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, WIDTH, HEIGHT);
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
    }

    public void toggleMusic(){
//        if(backgroundSound.isPlaying()){
//            backgroundSound.pause();
//        } else {
//            backgroundSound.play();
//        }
    }

    @Override
    public void dispose() {
        texture.dispose();
        gameSpriteBatch.dispose();
        pixmap.dispose();
        backgroundSound.dispose();
    }
}
