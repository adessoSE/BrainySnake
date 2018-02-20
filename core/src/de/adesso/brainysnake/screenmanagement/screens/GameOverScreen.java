package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.screenmanagement.ScreenManager;
import de.adesso.brainysnake.screenmanagement.ScreenType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Drawing the Game Over Screen. Contain the list of players sorted by the points and an button linking to the starting menu.
 */
public class GameOverScreen extends AbstractScreen {

    private static final int PLAYERNAMES_YOFFSET = 75;

    private GlyphLayout layout = new GlyphLayout();

    private TextButton returnButton;

    private int newLine = 0;

    private boolean isWinner = true;

    private boolean isSecond = !isWinner;

    private List<PlayerDTO> player;

    private ArrayList<PlayerDTO> deadPlayer;

    public GameOverScreen(List<PlayerDTO> player, ArrayList<PlayerDTO> deadPlayer) {
        super();
        this.player = player;
        this.deadPlayer = deadPlayer;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
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
    public SortedMap<Long, ArrayList<PlayerDTO>> createSortedWinnerMap() {
        SortedMap<Long, ArrayList<PlayerDTO>> sortedMap = new TreeMap<>();
        for (PlayerDTO playerHandler : player) {
            if (sortedMap.containsKey(playerHandler.getSize())) {
                sortedMap.get(playerHandler.getSize()).add(playerHandler);
            } else {
                ArrayList<PlayerDTO> playerHandlers = new ArrayList<PlayerDTO>() {{
                    add(playerHandler);
                }};
                sortedMap.put(playerHandler.getSize(), playerHandlers);
            }
        }
        if (!deadPlayer.isEmpty()) {
            sortedMap.put(0L, deadPlayer);
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
     * @param {@link #PlayerDTO} provides all the needed information about the player
     */
    public void drawWinnerScreenPlayerDetails(PlayerDTO playerDTO) {
        defaultFont.setColor(playerDTO.getColor());
        layout.setText(defaultFont, Long.toString(playerDTO.getSize()));
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2 + 250, (Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4) - PLAYERNAMES_YOFFSET * newLine);
        layout.setText(defaultFont, playerDTO.getName());
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2 - 50, (Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4) - PLAYERNAMES_YOFFSET * newLine++);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        getBatch().begin();
        defaultFont.getData().setScale(4, 4);
        defaultFont.setColor(0, 0, 0, 1);

        layout.setText(defaultFont, "Result of the round:");
        defaultFont.draw(getBatch(), layout, (Config.APPLICATION_WIDTH - layout.width) / 2, (Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4) + 75 * 2);

        defaultFont.getData().setScale(3, 3);

        SortedMap<Long, ArrayList<PlayerDTO>> sortedMap = createSortedWinnerMap();

        this.isWinner = true;
        this.isSecond = false;
        newLine = 0;
        for (int i = sortedMap.size() - 1; i >= 0; i--) {
            ArrayList<PlayerDTO> sortedMapValue = sortedMap.get(sortedMap.keySet().toArray()[i]);
            if (sortedMapValue.size() > 1) {
                for (PlayerDTO playerDTO : sortedMapValue) {
                    checkPositioningPlayer();
                    drawWinnerScreenPlayerDetails(playerDTO);
                }
            } else {
                checkPositioningPlayer();
                drawWinnerScreenPlayerDetails(sortedMapValue.get(0));
            }
        }

        getBatch().end();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
