package de.adesso.brainysnake.Gamelogic;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class Utils {

    public static Point2D fromGridPoint2(GridPoint2 gridPoint2) {
        return new Point2D(gridPoint2.x, gridPoint2.y);
    }

    public static Point2D fromVector2(Vector2 vector2) {
        return new Point2D((int)vector2.x,(int) vector2.y);
    }

}
