package de.adesso.brainysnake.renderer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.level.LevelDTO;
import de.adesso.brainysnake.renderer.level.LevelObject;

import java.util.ArrayList;
import java.util.List;

public class BrainySnake extends ApplicationAdapter {

    private SpriteBatch gameSpriteBatch;
    private Texture levelTexture;
    private Sprite levelSprite;
    private Pixmap levelMap;
    private Music backgroundSound;
    private LevelDTO levelDTO;
    private List<LevelObject> snakes;

    public BrainySnake() {
    }

    /**
     * Initializes the Level depending on the Application height/width and the dot size.
     * */
    public void initialize() {
        int height = Config.APPLICATION_HEIGHT / Config.DOT_SIZE;
        int width = Config.APPLICATION_WIDTH / Config.DOT_SIZE;
        this.levelDTO = new LevelDTO(width, height);
    }

    @Override
    public void create() {

        levelMap = new Pixmap(levelDTO.getWidth(), levelDTO.getHeight(), Pixmap.Format.RGBA8888);
        levelMap.setColor(Color.BLACK);
        levelMap.fill();

        levelTexture = new Texture(levelMap);
        levelSprite = new Sprite(levelTexture);

        backgroundSound = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
        startPlayingMusic(backgroundSound);

        OrthographicCamera mainCamera = new OrthographicCamera();
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

    /**
     * Calls the method drawGameLoop.
     * */
    @Override
    public void render() {
        drawGameLoop();
    }

    /**
     * Collects all Object, which should be renderen in level
     *
     * @return List<LevelObject> with alls Object, belong to the level
     */
    private List<LevelObject> createLevelObjects() {
        List<LevelObject> levelObjects = new ArrayList<>();
        LevelObject walls = levelDTO.getWalls();
        LevelObject barriers = levelDTO.getBarriers();
        LevelObject pointLabyrinths = levelDTO.getPointLabyrinths();
        LevelObject points = levelDTO.getPoints();

        if (walls != null) {
            levelObjects.add(walls);
        }

        if (barriers != null) {
            levelObjects.add(barriers);
        }

        if (barriers != null) {
            levelObjects.add(pointLabyrinths);
        }

        if (points != null) {
            levelObjects.add(points);
        }

        if (!snakes.isEmpty()) {
            levelObjects.addAll(snakes);
        }

        return levelObjects;
    }

    public void updateSnakes(List<LevelObject> levelObjects) {
        snakes = levelObjects;
    }

    public void updateLevelPoints(LevelObject points, LevelObject barriers, LevelObject walls, LevelObject pointLabyrinths) {
        levelDTO.setBarriers(barriers);
        levelDTO.setWalls(walls);
        levelDTO.setPoints(points);
        levelDTO.setPointLabyrinths(pointLabyrinths);
    }

    public void drawGameLoop() {
        // Redraw the head.
        levelMap.setColor(Color.BLACK);
        levelMap.fill();

        List<LevelObject> levelObjects = createLevelObjects();
        for (LevelObject levelObject : levelObjects) {
            levelMap.setColor(levelObject.getColor());
            for (Point2D tempDot : levelObject.getPositions()) {
                levelMap.drawPixel(tempDot.getX(), tempDot.getY());
            }
        }

        levelObjects.clear();

        // drawGameLoop pixmap to gameSpriteBatch
        levelTexture.draw(levelMap, 0, 0);
        gameSpriteBatch.begin();
        levelSprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();
    }

    @Override
    public void dispose() {
        levelTexture.dispose();
        gameSpriteBatch.dispose();
        levelMap.dispose();
        backgroundSound.dispose();
    }
}
