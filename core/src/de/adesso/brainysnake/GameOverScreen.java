package de.adesso.brainysnake;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Drawing the Game Over Screen. Contain the list of players sorted by the points and an button linking to the starting menu.
 */
public class GameOverScreen extends AbstractScreen {

    private static final int PLAYERNAMES_YOFFSET = 75;

    private static final int BUTTON_OFFSET = 25;

    private TextButton newGameButton, returnButton;

    private Image logoBrainySnake;

    private GlyphLayout layout = new GlyphLayout();

    private int newLine = 0;

    private boolean isWinner = true;

    private boolean isSecond = !isWinner;

    private List<PlayerHandler> playerHandlerList;

    private ArrayList<PlayerHandler> deadPlayer;

    GameOverScreen(List<PlayerHandler> playerHandlerList, ArrayList<PlayerHandler> deadPlayer) {
        this.playerHandlerList = playerHandlerList;
        this.deadPlayer = deadPlayer;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {

        getBatch().begin();
        defaultFont.getData().setScale(4, 4);
        defaultFont.setColor(0, 0, 0, 1);


        newGameButton = new TextButton("Restart Game", defaultSkin);
        newGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - 250f, Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().restartGame();
            }
        });
        newGameButton.setWidth(500f);
        addActor(newGameButton);

        layout.setText(defaultFont, "Result of the round:");
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2, newGameButton.getY() + PLAYERNAMES_YOFFSET * 2);

        defaultFont.getData().setScale(3, 3);

        SortedMap<Integer, ArrayList<PlayerHandler>> sortedMap = createSortedWinnerMap();

        this.isWinner = true;
        this.isSecond = false;
        newLine = 0;
        for (int i = sortedMap.size() - 1; i >= 0; i--) {
            ArrayList<PlayerHandler> sortedMapValue = sortedMap.get(sortedMap.keySet().toArray()[i]);
            if (sortedMapValue.size() > 1) {
                for (PlayerHandler playerHandler : sortedMapValue) {
                    checkPositioningPlayer();
                    drawWinnerScreenPlayerDetails(playerHandler);
                }
            } else {
                checkPositioningPlayer();
                drawWinnerScreenPlayerDetails(sortedMapValue.get(0));
            }
        }

        getBatch().end();

        returnButton = new TextButton("Back To Menu", defaultSkin);
        returnButton.setPosition(Config.APPLICATION_WIDTH / 2 - 125f, Config.APPLICATION_HEIGHT / 6);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().showScreen(ScreenType.MAIN_MENU);
            }
        });
        returnButton.setWidth(250f);
        addActor(returnButton);
    }

    /**
     * Creates a sorted Map of the players according to the points
     *
     * @return Keys are the score as Integer and the value is a ArrayList with the PlayerHandlers with this score
     */
    public SortedMap<Integer, ArrayList<PlayerHandler>> createSortedWinnerMap() {
        SortedMap<Integer, ArrayList<PlayerHandler>> sortedMap = new TreeMap<>();
        for (PlayerHandler playerHandler : playerHandlerList) {
            if (sortedMap.containsKey(playerHandler.getSnake().getAllSnakePositions().size())) {
                sortedMap.get(playerHandler.getSnake().getAllSnakePositions().size()).add(playerHandler);
            } else {
                ArrayList<PlayerHandler> playerHandlers = new ArrayList<PlayerHandler>() {{
                    add(playerHandler);
                }};
                sortedMap.put(playerHandler.getSnake().getAllSnakePositions().size(), playerHandlers);
            }
        }
        if (!deadPlayer.isEmpty()) {
            sortedMap.put(0, deadPlayer);
        }
        return sortedMap;
    }

    /**
     * Checks the players position. The winner gets highlighted.
     */
    public void checkPositioningPlayer() {
        if (isWinner) {
            isWinner = false;
            isSecond = true;
            defaultFont.getData().setScale(4, 4);
        } else if (isSecond) {
            isSecond = false;
            defaultFont.getData().setScale(3, 3);
            newLine++;
        }
    }

    /**
     * Draws the player name and points on the mainStage. Information gets extracted from PlayerHandler.
     *
     * @param playerHandler PlayerHandler provides all the needed information about the player
     */
    public void drawWinnerScreenPlayerDetails(PlayerHandler playerHandler) {
        defaultFont.setColor(playerHandler.getSnake().getHeadColor());
        layout.setText(defaultFont, playerHandler.getSnake().getAllSnakePositions().size() + "");
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2 + 250, newGameButton.getY() - PLAYERNAMES_YOFFSET * newLine);
        layout.setText(defaultFont, playerHandler.getPlayerName());
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2 - 50, newGameButton.getY() - PLAYERNAMES_YOFFSET * newLine++);
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
