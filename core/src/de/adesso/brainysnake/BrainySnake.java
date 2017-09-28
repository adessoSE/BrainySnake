package de.adesso.brainysnake;

import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Game;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class BrainySnake extends ApplicationAdapter {

    private Texture texture;
    private SpriteBatch batch;
    private Sprite sprite;
    private Pixmap pixmap;

    private static int DOT_SITZE = 10;
    private static int WIDTH = Config.APPLICATION_WIDTH / DOT_SITZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / DOT_SITZE;

    private OrthographicCamera cam;
    private OrthographicCamera fontCam;

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private float timeSinceLastRender;

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

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);

        draw();
    }

    private void initializeCamera() {
        cam = new OrthographicCamera();
        cam.setToOrtho(true, WIDTH, HEIGHT);
        fontCam = new OrthographicCamera();
        fontCam.setToOrtho(false, WIDTH, HEIGHT);
    }

    public void draw() {
        //Redraw the head.
        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        List<GameObject> gameObjects = game.draw(Gdx.graphics.getDeltaTime());
        for (GameObject gameObject : gameObjects) {
            pixmap.setColor(gameObject.getColor());
            for (Point2D tempDot : gameObject.getPositions()) {
                pixmap.drawPixel(tempDot.getX(), tempDot.getY());
            }
        }

        //draw pixmap to batch
        texture.draw(pixmap, 0, 0);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
        pixmap.dispose();
        //TODO rukl@rukl do all the dispose here
    }

}
