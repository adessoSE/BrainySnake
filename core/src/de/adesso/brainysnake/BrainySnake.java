package de.adesso.brainysnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.GameController;
import de.adesso.brainysnake.renderer.level.LevelDTO;
import de.adesso.brainysnake.renderer.level.LevelObject;
import de.adesso.brainysnake.screenmanagement.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class BrainySnake extends ApplicationAdapter {

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private int levelWidth;// = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
    private int levelHeight;// = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;

    private Texture texture;
    private SpriteBatch gameSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;
    private Music backgroundSound;
    private OrthographicCamera mainCamera;
    private float timeSinceLastRender = 0;

    private LevelDTO levelDTO;

    private GameController gameController = new GameController();

    public BrainySnake() {

    }

    public void initialize(int levelWidth, int levelHeight) {
        this.levelWidth = levelWidth;
        this.levelHeight = levelHeight;
        this.levelDTO = new LevelDTO(levelWidth, levelHeight);
    }

    public void updateLevelPoints() {

    }

    @Override
    public void create() {

        pixmap = new Pixmap(levelWidth, levelHeight, Pixmap.Format.RGBA8888);

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

    private void checkPressedKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            ScreenManager.getINSTANCE().togglePauseGame();
        }
    }

    @Override
    public void render() {
        checkPressedKeys();

        if (!ScreenManager.getINSTANCE().isGamePaused()) {
            timeSinceLastRender += Gdx.graphics.getDeltaTime();
            if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
                // Do the actual rendering, pass timeSinceLastRender as delta time.
                timeSinceLastRender = 0f;
            }
        }
        drawGameLoop();
    }

    private void initializeCamera() {

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, levelWidth, levelHeight);
    }

    public List<LevelObject> test() {
        List<LevelObject> levelObjects = new ArrayList<>();
        LevelObject walls = levelDTO.getWalls();
        LevelObject barriers = levelDTO.getBarriers();
        LevelObject points = levelDTO.getPoints();

        if (walls != null) {
            levelObjects.add(walls);
        }

        if (barriers != null) {
            levelObjects.add(barriers);
        }

        if (points != null) {
            levelObjects.add(points);
        }

        //todo rukl
         /*   for (PlayerHandler playerHandler : gameMaster.getPlayerHandler()) {
                Snake snake = playerHandler.getSnake();
                if (Config.RENDER_PLAYERVIEW && playerHandler.getPlayerView() != null) {
                    levelObjects.addAll(drawPlayerView(playerHandler.getPlayerView()));
                }
                levelObjects.add(snake.getHead());
                levelObjects.add(snake.getBody());

            }
            */

        return levelObjects;
    }

    public void drawGameLoop() {
        // Redraw the head.
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        List<LevelObject> levelObjects = test();
        for (LevelObject levelObject : levelObjects) {
            pixmap.setColor(levelObject.getColor());
            for (Point2D tempDot : levelObject.getPositions()) {
                pixmap.drawPixel(tempDot.getX(), tempDot.getY());
            }
        }
        levelObjects.clear();

        // drawGameLoop pixmap to gameSpriteBatch
        texture.draw(pixmap, 0, 0);
        gameSpriteBatch.begin();
        sprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();
    }

    public void toggleMusic() {
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
