package de.adesso.brainysnake.Gamelogic;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.Level;
import de.adesso.brainysnake.Gamelogic.Player.PlayerHandler;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.Field;
import de.adesso.brainysnake.playercommon.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    List<GameObject> gameObjects = new ArrayList<>();

    private GameMaster gameMaster;

    public void init(GameMaster gameMaster) {
        this.gameMaster = gameMaster;
    }

    public void update(float delta) {
        gameMaster.update(delta);
    }

    public List<GameObject> draw(float delta) {
        Level level = gameMaster.getLevel();
        gameObjects.add(level.getLevel());
        gameObjects.add(level.getBarriers());
        gameObjects.add(level.getPoints());
        for (PlayerHandler playerHandler : gameMaster.getPlayerHandler()) {
            Snake snake = playerHandler.getSnake();
            if (Config.RENDER_PLAYERVIEW && playerHandler.getPlayerView() != null) {
                gameObjects.addAll(drawPlayerView(playerHandler.getPlayerView()));
            }
            gameObjects.add(snake.getHead());
            gameObjects.add(snake.getBody());

        }

        return gameObjects;
    }

    public List<GameObject> drawPlayerView(PlayerView playerView) {
        List<GameObject> gameObject = new ArrayList<GameObject>();

        for (Field field : playerView.getVisibleFields()) {
            GameObject temp = new GameObject();
            temp.getPositions().add(field.getPosition());
            switch (field.getFieldType()) {
                case EMPTY:
                    temp.setColor(Color.DARK_GRAY);
                    break;
                case LEVEL:
                    temp.setColor(Color.GREEN);
                    break;
                case PLAYER:
                    temp.setColor(Color.PINK);
                    break;
                case POINT:
                    temp.setColor(Color.ORANGE);
                    break;
                default:
                    temp.setColor(Color.PINK);
            }

            gameObject.add(temp);
        }

        return gameObject;
    }

    public void reset() {
        gameObjects.clear();
    }


    public boolean isGameOver() {
        return gameMaster.isGameOver();
    }

}
