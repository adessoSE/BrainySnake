package de.adesso.brainysnake.gamelogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Return values of GridPoint2 as Point2D
     *
     * @param gridPoint2
     * @return GridPoint2 as Point2D
     */
    public static Point2D fromGridPoint2(GridPoint2 gridPoint2) {
        return new Point2D(gridPoint2.x, gridPoint2.y);
    }

    /**
     * Return values of Vector2 as Point2D
     *
     * @param vector2
     * @return Vector2 as Point2D
     */
    public static Point2D fromVector2(Vector2 vector2) {
        return new Point2D((int) vector2.x, (int) vector2.y);
    }

    /**
     * List of shuffled player Colors
     *
     * @param count Needed amount of Colors in List. Can not be more than Config.PLAYER_COLOR
     * @return List of Color
     */
    public static List<Color> getShuffledPlayerColor(int count) {
        List<Color> validPlayerColors = Config.PLAYER_COLORS;

        if (count > validPlayerColors.size()) {
            throw new IllegalStateException("Not enough valid Playercolor available");
        }

        List<Color> colorList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            colorList.add(new Color(validPlayerColors.get(i)));
        }

        return colorList;
    }

}
