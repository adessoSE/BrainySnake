package de.adesso.brainysnake.gamelogic.level;

import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.LinkedList;

/**
 * A labyrinth with a very large number of points
 */
public class PointLabyrinth {

    private static final int[][] layout = {
            {2, 2, 1, 1, 1, 1, 1, 1, 2},
            {2, 1, 0, 0, 0, 1, 1, 1, 1},
            {2, 1, 0, 1, 0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1, 1, 0, 0, 1},
            {1, 1, 0, 0, 0, 1, 1, 0, 1},
            {2, 2, 1, 1, 0, 1, 1, 0, 0},
            {2, 2, 1, 0, 0, 1, 1, 1, 1},
            {2, 1, 1, 0, 1, 0, 0, 0, 1},
            {2, 1, 1, 0, 1, 0, 1, 0, 1},
            {2, 2, 1, 0, 0, 0, 1, 0, 0},
            {2, 2, 1, 1, 1, 1, 1, 1, 1}
    };

    private LinkedList<Point2D> labyrinthDots = new LinkedList<>();

    /**
     * Positions in Labyrinth to spawn points at
     */
    private LinkedList<Point2D> pointPositions = new LinkedList<>();

    public PointLabyrinth(int x, int y) {
        buildLabyrinth(x, y);
    }

    /**
     * TODO rukl doc
     */
    private void buildLabyrinth(int x, int y) {

        for (int tempY = 0; tempY < layout.length; tempY++) {
            for (int tempX = 0; tempX < layout[0].length; tempX++) {
                int i = layout[tempY][tempX];
                if (i == 1) {
                    labyrinthDots.add(new Point2D(tempX + x, tempY + y));
                } else if (i == 0) {
                    pointPositions.add(new Point2D(tempX + x, tempY + y));
                }
            }
        }
    }

    public LinkedList<Point2D> getLabyrinthDots() {
        return labyrinthDots;
    }

    public LinkedList<Point2D> getPointPositions() {
        return pointPositions;
    }
}
