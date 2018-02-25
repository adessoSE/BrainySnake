package de.adesso.brainysnake.renderer.level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds information about level objects for the renderer
 */
public class LevelDTO {

    private int width, height;

    private LevelObject walls;

    private LevelObject barriers;

    private LevelObject points;

    private LevelObject pointLabyrinths;

    public LevelDTO(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public LevelObject getWalls() {
        return walls;
    }

    public void setWalls(LevelObject walls) {
        this.walls = walls;
    }

    public LevelObject getBarriers() {
        return barriers;
    }

    public void setBarriers(LevelObject barriers) {
        this.barriers = barriers;
    }

    public LevelObject getPoints() {
        return points;
    }

    public void setPoints(LevelObject points) {
        this.points = points;
    }

    public LevelObject getPointLabyrinths() {
        return pointLabyrinths;
    }

    public void setPointLabyrinths(LevelObject pointLabyrinths) {
        this.pointLabyrinths = pointLabyrinths;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
