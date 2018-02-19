package de.adesso.brainysnake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import de.adesso.brainysnake.Gamelogic.GameMaster;
import de.adesso.brainysnake.Gamelogic.IO.KeyBoardControl;

public class GameScreen extends AbstractScreen {

    private BrainySnake brainySnake;

    GameScreen(GameMaster gameMaster) {
        brainySnake = new BrainySnake(gameMaster);
    }

    @Override
    public void show() {
    }

    @Override
    public void initialize() {
        brainySnake.create();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        brainySnake.render();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
