package de.adesso.brainysnake.renderer.level;


import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.LinkedList;

/**
 * Represents a renderable object for game
 */
public class LevelObject {

    protected LinkedList<Point2D> positions;

    protected Color color;

    public LevelObject() {
        positions = new LinkedList<>();
        color = Config.COLOR_404;
    }

    public LevelObject(LinkedList<Point2D> positions) {
        this(positions, Color.MAROON);
    }

    public LevelObject(LinkedList<Point2D> positions, Color color) {
        if (positions == null) {
            this.positions = new LinkedList<Point2D>();
        } else {
            this.positions = positions;
        }

        this.color = color;
    }

    public LinkedList<Point2D> getPositions() {
        return positions;
    }

    public void setPositions(LinkedList<Point2D> positions) {
        this.positions = positions;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int size() {
        return positions.size();
    }
}
