package de.adesso.brainysnake.Gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.*;

public class Utils {

    public static Point2D fromGridPoint2(GridPoint2 gridPoint2) {
        return new Point2D(gridPoint2.x, gridPoint2.y);
    }

    public static Point2D fromVector2(Vector2 vector2) {
        return new Point2D((int)vector2.x,(int) vector2.y);
    }

    public static List<Color> getValidPlayerColors() {
        List<Color> playerColors = new ArrayList<Color>();
        playerColors.add(Color.BLUE);
        playerColors.add(Color.GREEN);
        playerColors.add(Color.ORANGE);
        playerColors.add(Color.CYAN);
        return playerColors;
    }

    public static List<Color> getShuffledGameColors() {
        List<Color> validPlayerColors = getValidPlayerColors();
        Collections.shuffle(validPlayerColors);
        return validPlayerColors;
    }

}
