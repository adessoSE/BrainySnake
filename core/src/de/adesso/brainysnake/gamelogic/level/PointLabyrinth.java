package de.adesso.brainysnake.gamelogic.level;

import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A labyrinth with a very large number of points
 */
public class PointLabyrinth {

    private LinkedList<Point2D> labyrinthDots = new LinkedList<>();

    /**
     * Positions in Labyrinth to spawn points at
     */
    private LinkedList<Point2D> pointPositions =  new LinkedList<>();

    public PointLabyrinth(int x, int y) {
        buildLabyrinth(x, y);
    }

    /**
     * TODO rukl doc
     */
    private void buildLabyrinth(int x, int y) {
        labyrinthDots.add(new Point2D(x, y + 1));
        labyrinthDots.add(new Point2D(x, y));
        labyrinthDots.add(new Point2D(x, y - 1));

        labyrinthDots.add(new Point2D(x + 1, y + 1));
        labyrinthDots.add(new Point2D(x + 2, y));
        labyrinthDots.add(new Point2D(x + 3, y - 1));

        labyrinthDots.add(new Point2D(x - 1, y + 1));
        labyrinthDots.add(new Point2D(x - 1, y));
        labyrinthDots.add(new Point2D(x - 1, y - 1));

        //Generate positions where points can be spawned
        pointPositions.add(new Point2D(x - 2, y + 1));
        pointPositions.add(new Point2D(x - 2, y));
        pointPositions.add(new Point2D(x - 2, y - 1));
    }

    public LinkedList<Point2D> getLabyrinthDots() {
        return labyrinthDots;
    }

    public LinkedList<Point2D> getPointPositions() {
        return pointPositions;
    }
}
