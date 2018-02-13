package de.adesso.brainysnake.Gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.*;

public class Utils {

    /**
     * Return values of GridPoint2 as Point2D
     * @param gridPoint2
     * @return GridPoint2 as Point2D
     */
    public static Point2D fromGridPoint2(GridPoint2 gridPoint2) {
        return new Point2D(gridPoint2.x, gridPoint2.y);
    }

    /**
     * Return values of Vector2 as Point2D
     * @param vector2
     * @return Vector2 as Point2D
     */
    public static Point2D fromVector2(Vector2 vector2) {
        return new Point2D((int)vector2.x,(int) vector2.y);
    }

    /**
     * @return List of four valid Colors for Player
     */
    public static List<Color> getValidPlayerColors() {
        List<Color> playerColors = new ArrayList<>();
        playerColors.add(Color.BLUE);
        playerColors.add(Color.GREEN);
        playerColors.add(Color.ORANGE);
        playerColors.add(Color.CYAN);
        return playerColors;
    }

    /**
     * @return List of four shuffled Player Colors {@link #getValidPlayerColors()}
     */
    public static List<Color> getShuffledGameColors() {
        List<Color> validPlayerColors = getValidPlayerColors();
        Collections.shuffle(validPlayerColors);
        return validPlayerColors;
    }

}
