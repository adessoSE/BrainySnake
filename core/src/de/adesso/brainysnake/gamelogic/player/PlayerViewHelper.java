package de.adesso.brainysnake.gamelogic.player;

import java.util.ArrayList;
import java.util.List;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;

import static de.adesso.brainysnake.Config.PLAYERVIEW_OFFSET_TO_AHEAD;
import static de.adesso.brainysnake.Config.PLAYERVIEW_OFFSET_TO_LEFT;

public class PlayerViewHelper {

    public static List<Point2D> generatePlayerView(Orientation orientation, Point2D position) {
        List<Point2D> view = new ArrayList<>();

        // Find the first
        Point2D leftTopCornerPositionOfView = getLeftTopCornerPositionOfView(orientation, position, PLAYERVIEW_OFFSET_TO_LEFT, PLAYERVIEW_OFFSET_TO_AHEAD);

        int width = Config.PLAYERVIEW_OFFSET_TO_VIEWWIDTH;
        int height = Config.PLAYERVIEW_OFFSET_TO_AHEAD;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (orientation) {
                    case UP:
                        view.add(new Point2D(leftTopCornerPositionOfView.x + x, leftTopCornerPositionOfView.y - y));
                        break;
                    case DOWN:
                        view.add(new Point2D(leftTopCornerPositionOfView.x - x, leftTopCornerPositionOfView.y + y));
                        break;
                    case LEFT:
                        view.add(new Point2D(leftTopCornerPositionOfView.x + y, leftTopCornerPositionOfView.y + x));
                        break;
                    case RIGHT:
                        view.add(new Point2D(leftTopCornerPositionOfView.x - y, leftTopCornerPositionOfView.y - x));
                        break;
                }
            }
        }

        return view;
    }

    public static Point2D getLeftTopCornerPositionOfView(final Orientation orientation, final Point2D position, int xOffset, int yOffset) {
        Point2D currentPosition = position.cpy();

        switch (orientation) {
            case LEFT:
                currentPosition.add(-yOffset, -xOffset);
                break;
            case RIGHT:
                currentPosition.add(yOffset, xOffset);
                break;
            case UP:
                currentPosition.add(-xOffset, yOffset);
                break;
            case DOWN:
                currentPosition.add(xOffset, -yOffset);
                break;
        }

        return currentPosition;
    }
}
