package de.adesso.brainysnake.Gamelogic.Level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class Level {

    private int width, height;

    private GameObject levelObject;

    private GameObject barriers;

    private GameObject points;

    private int maxPointsInLevel = Config.MAX_POINTS_IN_LEVEL;

    public Level(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        levelObject = new GameObject(buildOuterWalls(), Config.LEVEL_COLOR);
        barriers = new GameObject(buildBarriers(), Config.BARRIER_COLOR);
        points = new GameObject(null, Config.POINT_COLLOR);
    }

    private LinkedList<Point2D> buildBarriers() {
        LinkedList<Point2D> barriers = new LinkedList<Point2D>();
        int gapBarriersX = width / 4;
        int gapBarriersY = height / 3;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                barriers.addAll(addBarrier((x + 1) * gapBarriersX, (y + 1) * gapBarriersY));
            }
        }

        return barriers;
    }

    private List<Point2D> addBarrier(int x, int y) {
        List<Point2D> barrierDots = new ArrayList<Point2D>();

        barrierDots.add(new Point2D(x, y + 1));
        barrierDots.add(new Point2D(x, y));
        barrierDots.add(new Point2D(x, y - 1));

        barrierDots.add(new Point2D(x + 1, y + 1));
        barrierDots.add(new Point2D(x + 1, y));
        barrierDots.add(new Point2D(x + 1, y - 1));

        barrierDots.add(new Point2D(x - 1, y + 1));
        barrierDots.add(new Point2D(x - 1, y));
        barrierDots.add(new Point2D(x - 1, y - 1));
        return barrierDots;
    }

    private LinkedList<Point2D> buildOuterWalls() {
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        for (int x = 0; x < width; x++) {
            // wall bottom
            points.add(new Point2D(x, 0));
            // wall top
            points.add(new Point2D(x, height - 1));
        }

        for (int y = 1; y < width; y++) {
            // wall left
            points.add(new Point2D(0, y));
            // wall right
            points.add(new Point2D(width - 1, y));
        }

        return points;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GameObject getLevel() {
        return levelObject;
    }

    public GameObject getBarriers() {
        return barriers;
    }

    public GameObject getPoints() {
        return points;
    }

    /**
     * @param x
     *            positions in Level
     * @param y
     *            positions in Level
     * @return do positions hit levelwall or barrier
     */
    public boolean checkCollision(int x, int y) {

        // check if doc collides with level element
        for (Point2D tempPoint2D : levelObject.getPositions()) {
            if (tempPoint2D.getX() == x && tempPoint2D.getY() == y) {
                return true;
            }
        }

        for (Point2D tempPoint2D : barriers.getPositions()) {
            if (tempPoint2D.getX() == x && tempPoint2D.getY() == y) {
                return true;
            }
        }

        return false;
    }

    public Snake createStartingGameObject(Orientation orientation, int initialLength) {
        LinkedList<Point2D> head = new LinkedList<Point2D>();
        LinkedList<Point2D> body = new LinkedList<Point2D>();

        int centerX = (int) Math.floor(this.width / 2D);
        int centerY = (int) Math.floor(this.height / 2D);

        for (int i = 0; i <= initialLength; i++) {
            Point2D positionIn = getPositionIn(orientation, centerX, centerY, i);
            if (i == initialLength) {
                head.addFirst(positionIn);
            } else {
                body.addFirst(positionIn);
            }
        }
        return new Snake(new GameObject(head), new GameObject(body));
    }

    private Point2D getPositionIn(Orientation orientation, int centerX, int centerY, int length) {
        int offset = 5;
        switch (orientation) {
            case UP:
                return new Point2D(centerX + offset, centerY + length + 1);
            case DOWN:
                return new Point2D(centerX - offset, centerY - length - 1);
            case RIGHT:
                return new Point2D(centerX + length + 1, centerY + offset);
            case LEFT:
                return new Point2D(centerX - length - 1, centerY - offset);
            default:
                return null;
        }
    }

    public void spreadPoints() {
        for (int i = 0; i < (maxPointsInLevel - points.size()); i++) {
            Point2D randomLevelPosition = null;
            do {
                randomLevelPosition = getRandomLevelPosition();
            } while (randomLevelPosition == null || checkCollision(randomLevelPosition.x, randomLevelPosition.y));
            points.getPositions().add(new Point2D(randomLevelPosition.x, randomLevelPosition.y));
        }
    }

    private Point2D getRandomLevelPosition() {
        int randomXPosition = 0 + (int) (Math.random() * width);
        int randomYPosition = 0 + (int) (Math.random() * height);
        return new Point2D(randomXPosition, randomYPosition);
    }

    public boolean tryConsumePoint(Point2D position) {
        for (Point2D Point2D : points.getPositions()) {
            if (Point2D.x == position.x && Point2D.y == position.y) {
                points.getPositions().remove(Point2D);
                return true;
            }
        }

        return false;
    }

}
