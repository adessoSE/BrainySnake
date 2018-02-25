package de.adesso.brainysnake.screenmanagement.screens;


import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.GameBoard;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.PlayerBoard;

import java.util.List;

//TODO move Pause-Screen to this Screen
public class GameScreen extends AbstractScreen {

    private GameMaster gameMaster;

    public GameScreen() {
        gameMaster = new GameMaster(GameBoard.getINSTANCE());
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void initialize() {
    }

    @Override
    public void render(float delta) {
        gameMaster.update(delta);
        drawGameUI();
        super.act(delta);
        super.draw();
    }

    /**
     * Draw the game ui.
     * Contains the player names including points reached so far
     */
    private void drawGameUI() {
        getBatch().begin();
        defaultFont.getData().setScale(1f);

        defaultFont.setColor(Color.WHITE);
        defaultFont.draw(getBatch(), "Rounds remaining: ", 20, Config.APPLICATION_HEIGHT - 20);
        defaultFont.draw(getBatch(), GameBoard.getINSTANCE().getRemainingRoundsToPlay() + "", 165, Config.APPLICATION_HEIGHT - 20);

        List<PlayerBoard> playerBoards = GameBoard.getINSTANCE().getPlayerBoards();
        int offset = 20;
        for (PlayerBoard playerBoard : playerBoards) {
            defaultFont.setColor(playerBoard.getColor());
            defaultFont.draw(getBatch(), playerBoard.getName(), 20, Config.APPLICATION_HEIGHT - 20 - offset);
            defaultFont.draw(getBatch(), playerBoard.getPoints() + "", 165, Config.APPLICATION_HEIGHT - 20 - offset);
            offset += 20;
        }

        getBatch().end();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
     //   brainySnake.toggleMusic();
    }


}
