package de.adesso.brainysnake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.level.LevelDTO;
import de.adesso.brainysnake.renderer.level.LevelObject;

import java.util.ArrayList;
import java.util.List;

public class BrainySnake extends ApplicationAdapter {

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private Texture texture;
    private SpriteBatch gameSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;
    private Music backgroundSound;
    private OrthographicCamera mainCamera;
    private float timeSinceLastRender = 0;

    private LevelDTO levelDTO;
    private List<LevelObject> snakes;

    public BrainySnake() {

    }

    public void initialize() {
        int height = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;
        int width = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
        this.levelDTO = new LevelDTO(width, height);
    }

    public void updateSnakes(List<LevelObject> levelObjects) {
        snakes = levelObjects;
    }

    //todo rukl split in methods
    public void updateLevelPoints(LevelObject points, LevelObject barriers, LevelObject walls) {
        levelDTO.setBarriers(barriers);
        levelDTO.setWalls(walls);
        levelDTO.setPoints(points);
    }

    @Override
    public void create() {

        pixmap = new Pixmap(levelDTO.getWidth(), levelDTO.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        texture = new Texture(pixmap);
        sprite = new Sprite(texture);

        backgroundSound = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
        //   startPlayingMusic(backgroundSound);

        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, levelDTO.getWidth(), levelDTO.getHeight());

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

    @Override
    public void render() {
        timeSinceLastRender += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
            // Do the actual rendering, pass timeSinceLastRender as delta time.
            timeSinceLastRender = 0f;
        }

        drawGameLoop();
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

        levelObjects.addAll(snakes);


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
