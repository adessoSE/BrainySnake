package de.adesso.brainysnake;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Game;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;
import de.adesso.brainysnake.Gamelogic.Player.PlayerController;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.Gamelogic.UI.UIPlayerInformation;
import de.adesso.brainysnake.Gamelogic.UI.UiState;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrainySnake extends ApplicationAdapter {

    private Texture texture;
    private Texture logoTexture;
    private Image logoBrainySnake;
    private SpriteBatch gameSpriteBatch;
    private SpriteBatch fontSpriteBatch;
    private Sprite sprite;
    private Pixmap pixmap;

    private InputMultiplexer inputMultiplexer;
    private Stage mainStage;
    private Skin skin;

    private GameMaster gameMaster;

    private final int NAME_OFFSET = 75;
    private final int BUTTON_OFFSET = 25;
    private static int DOT_SIZE = 10;
    private static int WIDTH = Config.APPLICATION_WIDTH / DOT_SIZE;
    private static int HEIGHT = Config.APPLICATION_HEIGHT / DOT_SIZE;
    private static int APPLICATION_WIDTH = Config.APPLICATION_WIDTH;
    private static int APPLICATION_HEIGHT = Config.APPLICATION_HEIGHT;

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerController.class.getName());

    private OrthographicCamera mainCamera;
    private OrthographicCamera fontCamera;

    private TextButton returnButton;
    private TextButton newGameButton;
    private TextButton startGameButton;
    private TextButton exitButton;

    private static final float MIN_FRAME_LENGTH = 1f / Config.UPDATE_RATE;
    private float timeSinceLastRender = 0;

    private BitmapFont font;
    private List<GameObject> gameObjects;
    private Game game;
    private boolean menuShowing = true;
    private boolean matchMenuShowing = false;

    @Override
    public void create() {
        pixmap = new Pixmap(WIDTH, HEIGHT, Pixmap.Format.RGBA8888);
        mainStage = new Stage();
        createBasicSkin();

        logoTexture = new Texture("BrainySnake_Headline.png");
        TextureRegion region = new TextureRegion(logoTexture, 0, 0, 629, 180);
        logoBrainySnake = new Image(region);
        logoBrainySnake.setPosition(Config.APPLICATION_WIDTH / 4, Config.APPLICATION_HEIGHT - 250);


        texture = new Texture(pixmap);

        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        sprite = new Sprite(texture);

        initializeCamera();

        game = new Game();
        gameMaster = game.init(HEIGHT, WIDTH);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mainStage);
        inputMultiplexer.addProcessor(new KeyBoardControl(game));
        Gdx.input.setInputProcessor(inputMultiplexer);

        gameSpriteBatch = new SpriteBatch();
        gameSpriteBatch.setProjectionMatrix(mainCamera.combined);

        fontSpriteBatch = new SpriteBatch();
        fontSpriteBatch.setProjectionMatrix(fontCamera.combined);
        font = new BitmapFont();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (menuShowing) {
            drawStartScreen();
            return;
        }

        if (matchMenuShowing) {
            drawMatchScreen();
            return;
        }

        timeSinceLastRender += Gdx.graphics.getDeltaTime();
        if (timeSinceLastRender >= MIN_FRAME_LENGTH) {
            // Do the actual rendering, pass timeSinceLastRender as delta time.
            timeSinceLastRender = 0f;
            game.update(Gdx.graphics.getDeltaTime());
        }
        if (game.isGameOver()) {
            drawGameOverScreen();
            return;
        }

        drawGameLoop();
    }

    private void initializeCamera() {
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(true, WIDTH, HEIGHT);
        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, APPLICATION_WIDTH, APPLICATION_HEIGHT);
    }

    public void drawMatchScreen(){
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        newGameButton = new TextButton("Start Game", skin);
        newGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - 250f, Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    matchMenuShowing = false;
            }
        });
        newGameButton.setWidth(500f);
        mainStage.addActor(newGameButton);

        this.mainStage.getBatch().begin();
        font.getData().setScale(3,3);
        font.setColor(0,0,0,1);

        font.draw(this.mainStage.getBatch(), "Amount of rounds: " +   Config.MAX_ROUNDS, Config.APPLICATION_WIDTH/2 - 225f , newGameButton.getY() + NAME_OFFSET*2);

        if (!gameMaster.getPlayerController().getPlayerHandlerList().isEmpty()){
            int i = 1;
            for (PlayerHandler playerHandler : gameMaster.getPlayerController().getPlayerHandlerList()) {
                font.setColor(playerHandler.getSnake().getHeadColor());
                font.draw(this.mainStage.getBatch(), playerHandler.getPlayerName() , Config.APPLICATION_WIDTH/2 - 200f , newGameButton.getY() - NAME_OFFSET*i);
                i++;
            }
        }
        this.mainStage.getBatch().end();

        returnButton = new TextButton("Back To Menu", skin);
        returnButton.setPosition(Config.APPLICATION_WIDTH / 2 - 125f, Config.APPLICATION_HEIGHT / 6);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(Actor actor : mainStage.getActors()){
                    actor.setVisible(false);
                }

                menuShowing = true;
                matchMenuShowing = false;
            }
        });
        returnButton.setWidth(250f);
        mainStage.addActor(returnButton);

        mainStage.act();
        mainStage.draw();
    }

    public void drawStartScreen() {
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        mainStage.addActor(logoBrainySnake);

        startGameButton = new TextButton("Start Game", skin);
        startGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                for(Actor actor : mainStage.getActors()){
                    actor.setVisible(false);
                }
                menuShowing = false;
                matchMenuShowing = true;
            }
        });
        mainStage.addActor(startGameButton);

        exitButton = new TextButton("Exit", skin);
        exitButton.setPosition(Config.APPLICATION_WIDTH / 2 - Config.APPLICATION_WIDTH / 15, Config.APPLICATION_HEIGHT / 2 - 2 * (startGameButton.getHeight() + BUTTON_OFFSET));
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        mainStage.addActor(exitButton);

        mainStage.act();
        mainStage.draw();
    }

    private void createBasicSkin() {
        //Create a font
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        skin.add("default", font);

        //Create a texture
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        //Create a button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

    }
    public void drawGameLoop() {
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

        // drawGameLoop pixmap to gameSpriteBatch
        texture.draw(pixmap, 0, 0);
        gameSpriteBatch.begin();
        sprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();


        fontSpriteBatch.begin();
        font.getData().setScale(1f);

        font.setColor(Color.WHITE);
        font.draw(fontSpriteBatch, "Rounds remaining: ", 20, APPLICATION_HEIGHT - 20 );
        font.draw(fontSpriteBatch, UiState.getINSTANCE().getRoundsRemaining(), 165, APPLICATION_HEIGHT - 20 );

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

    public void drawGameOverScreen() {

        pixmap.setColor(Color.BLACK);
        pixmap.fill();

        texture.draw(pixmap, 0, 0);
        gameSpriteBatch.begin();
        sprite.draw(gameSpriteBatch);
        gameSpriteBatch.end();

        fontSpriteBatch.begin();
        font.getData().setScale(7f);
        font.setColor(Color.WHITE);
        font.draw(fontSpriteBatch, "Game Over", 20, APPLICATION_HEIGHT - 20);

        int offset = 150;

        font.getData().setScale(5f);
        HashMap<String, UIPlayerInformation> playerMap = UiState.getINSTANCE().getPlayerMap();
        for (String winnerName : playerMap.keySet()) {
            font.setColor(playerMap.get(winnerName).getColor());
            font.draw(fontSpriteBatch, winnerName, 20, APPLICATION_HEIGHT - offset);
            font.draw(fontSpriteBatch, playerMap.get(winnerName).getPoints(), 750, APPLICATION_HEIGHT - offset);
            offset += 120;
        }

        fontSpriteBatch.end();
    }

    @Override
    public void dispose() {
        try {
            texture.dispose();
            gameSpriteBatch.dispose();
            pixmap.dispose();
        } catch (Exception e){
            LOGGER.error(e.toString());
        }

    }

}
