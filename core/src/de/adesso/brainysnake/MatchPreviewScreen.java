package de.adesso.brainysnake;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;

import java.util.List;

/**
 * Drawing the MatchScreen. Contains the button "Start Game" and "Back to Menu", also displays the number of rounds and the names of the player.
 */
public class MatchPreviewScreen extends AbstractScreen {

    private final int PLAYERNAMES_YOFFSET = 75;

    private TextButton returnButton;

    private TextButton newGameButton;

    private List<PlayerHandler> playerHandlerList;

    MatchPreviewScreen(List<PlayerHandler> playerHandlerList) {
        this.playerHandlerList = playerHandlerList;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {

        newGameButton = new TextButton("Start Game", defaultSkin);
        newGameButton.setPosition(Config.APPLICATION_WIDTH / 2 - 250f, Config.APPLICATION_HEIGHT - Config.APPLICATION_HEIGHT / 4);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ScreenManager.getINSTANCE().startGame();
            }
        });
        newGameButton.setWidth(500f);
        addActor(newGameButton);

        getBatch().begin();
        drawTitle("Amount of rounds: " + Config.MAX_ROUNDS, this.newGameButton.getY());
        drawAllPlayerNames(this.newGameButton.getY());
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
     * Drawing a title specified in text on a given stage on top of a given button
     *
     * @param positionY The position where the title should be drawn
     * @param text      The text which should be drawn in the title
     */
    private void drawTitle(String text, Float positionY) {
        defaultFont.getData().setScale(3, 3);
        defaultFont.setColor(0, 0, 0, 1);
        defaultFont.draw(getBatch(), text, Config.APPLICATION_WIDTH / 2 - 225f, positionY + PLAYERNAMES_YOFFSET * 2);
    }

    /**
     * Drawing all PlayerNames from the GameMaster under the given button on the given stage
     *
     * @param positionY The position where the names should be drawn
     */
    private void drawAllPlayerNames(Float positionY) {
        if (playerHandlerList.isEmpty()) {
            return;
        }

        int i = 1;
        for (PlayerHandler playerHandler : playerHandlerList) {
            defaultFont.setColor(playerHandler.getSnake().getHeadColor());
            defaultFont.draw(getBatch(), playerHandler.getPlayerName(), Config.APPLICATION_WIDTH / 2 - 200f, positionY - PLAYERNAMES_YOFFSET * i++);
        }
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