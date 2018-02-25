package de.adesso.brainysnake.gamelogic.level;

import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Square obstacle for a snake
 */
public class Barrier {

    private List<Point2D> barrierDots = new ArrayList<>();

    public Barrier(int x, int y) {
        buildBarrier(x, y);
    }

    /**
     * @param x x-coordinate of the barrier's center
     * @param y y-coordinate of the barrier's center
     */
    private void buildBarrier(int x, int y) {
        barrierDots.add(new Point2D(x, y + 1));
        barrierDots.add(new Point2D(x, y));
        barrierDots.add(new Point2D(x, y - 1));

        barrierDots.add(new Point2D(x + 1, y + 1));
        barrierDots.add(new Point2D(x + 1, y));
        barrierDots.add(new Point2D(x + 1, y - 1));

        barrierDots.add(new Point2D(x - 1, y + 1));
        barrierDots.add(new Point2D(x - 1, y));
        barrierDots.add(new Point2D(x - 1, y - 1));
    }

    public List<Point2D> getBarrierDots() {
        return barrierDots;
    }

}
