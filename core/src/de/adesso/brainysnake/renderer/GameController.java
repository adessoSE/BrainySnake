package de.adesso.brainysnake.renderer;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.renderer.level.LevelObject;
import de.adesso.brainysnake.playercommon.Field;
import de.adesso.brainysnake.playercommon.PlayerView;

import java.util.ArrayList;
import java.util.List;

public class GameController {




    public List<LevelObject> drawPlayerView(PlayerView playerView) {
        List<LevelObject> levelObject = new ArrayList<>();

        for (Field field : playerView.getVisibleFields()) {
            LevelObject temp = new LevelObject();
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

            levelObject.add(temp);
        }

        return levelObject;
    }
}
