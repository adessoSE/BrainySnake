package de.adesso.brainysnake.renderer.level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO rukl
 */
public class LevelDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelDTO.class.getName());

    private int width, height;

    private LevelObject walls;

    private LevelObject barriers;

    private LevelObject points;

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
}
