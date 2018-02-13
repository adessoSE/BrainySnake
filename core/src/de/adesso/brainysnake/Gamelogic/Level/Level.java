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
        LinkedList<Point2D> barriers = new LinkedList<>();
        List<Point2D> barrierCenters = new ArrayList<>();

        // Number of barriers to be created, defined in config file
        outer_loop:
        for (int x = 0; x < Config.QUANTITY_BARRIERS; x++) {
            int counter = 0;

            Point2D newBarrierPosition = generateBarrierPosition();

            barrierCenters.add(newBarrierPosition);

            // If a "new point" has a spatial conflict with an existing point, generate a new "new point" and check again
            // If after a certain amount of tries (50) no fitting new point is found, stop adding more points -> break
            while (isConflicting(barrierCenters, newBarrierPosition)) {
                if (counter > 50) {
                    break outer_loop;
                }
                barrierCenters.remove(newBarrierPosition);
                newBarrierPosition = generateBarrierPosition();
                barrierCenters.add(newBarrierPosition);
                counter++;
            }

            barriers.addAll(addBarrier(newBarrierPosition.getX(), newBarrierPosition.getY()));
        }

        return barriers;
    }

    // Generate a random position for the center of a barrier
    private Point2D generateBarrierPosition() {
        double xRandom = Math.random();
        double yRandom = Math.random();

        int xCenter = (int) (xRandom * width);
        int yCenter = (int) (yRandom * height);

        // Make sure barriers don't overlap with level borders
        if (xCenter > (width - 3)) {
            xCenter = xCenter - 3;
        } else if (xCenter < 2) {
            xCenter = xCenter + 2;
        }
        if (yCenter > (height - 3)) {
            yCenter = yCenter - 3;
        } else if (yCenter < 2) {
            yCenter = yCenter + 2;
        }


        return new Point2D(xCenter, yCenter);
    }

    // See if new barrier position is too close to existing barrier
    private boolean isConflicting(List<Point2D> points, Point2D newPoint) {
        for (Point2D point : points) {
            if (!point.equals(newPoint)) {
                if (point.dst(newPoint) < Config.DISTANCE_BETWEEN_BARRIERS) {
                    return true;
                }
            }
        }
        return false;
    }


    private List<Point2D> addBarrier(int x, int y) {
        List<Point2D> barrierDots = new ArrayList<>();

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
        LinkedList<Point2D> positionts = new LinkedList<Point2D>();
        for (int x = 0; x < width; x++) {
            // wall bottom
            positionts.add(new Point2D(x, 0));
            // wall top
            positionts.add(new Point2D(x, height - 1));
        }

        for (int y = 1; y < width; y++) {
            // wall left
            positionts.add(new Point2D(0, y));
            // wall right
            positionts.add(new Point2D(width - 1, y));
        }

        return positionts;
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

    public boolean checkCollision(Point2D point2D) {
        return levelObject.getPositions().contains(point2D) || barriers.getPositions().contains(point2D);
    }

    public Snake createStartingGameObject(Orientation orientation, int initialLength) {
        LinkedList<Point2D> head = new LinkedList<Point2D>();
        LinkedList<Point2D> body = new LinkedList<Point2D>();

        int centerX = (int) Math.floor(this.width / 2D);
        int centerY = (int) Math.floor(this.height / 2D);

        for (int i = 1; i <= initialLength; i++) {
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
                return new Point2D(centerX + offset, centerY + length + Config.STARTING_POSITION_SPACE);
            case DOWN:
                return new Point2D(centerX - offset, centerY - length - Config.STARTING_POSITION_SPACE);
            case RIGHT:
                return new Point2D(centerX + length + Config.STARTING_POSITION_SPACE, centerY + offset);
            case LEFT:
                return new Point2D(centerX - length - Config.STARTING_POSITION_SPACE, centerY - offset);
            default:
                return null;
        }
    }

    public void spreadPoints() {
        for (int i = 0; i < (maxPointsInLevel - points.size()); i++) {
            Point2D randomLevelPosition = null;
            do {
                randomLevelPosition = getRandomLevelPosition();
            } while (randomLevelPosition == null || checkCollision(randomLevelPosition));
            points.getPositions().add(new Point2D(randomLevelPosition.x, randomLevelPosition.y));
        }
    }

    private Point2D getRandomLevelPosition() {
        int randomXPosition = 0 + (int) (Math.random() * width);
        int randomYPosition = 0 + (int) (Math.random() * height);
        return new Point2D(randomXPosition, randomYPosition);
    }

    public boolean tryConsumePoint(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.equals(position)) {
                return points.getPositions().remove(point2D);
            }
        }

        return false;
    }

    public boolean isPointOn(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.x == position.x && point2D.y == position.y) {
                return true;
            }
        }

        return false;
    }

    public boolean levelContainsPosition(Point2D point2D) {
        return point2D.x >= 0 && point2D.y >= 0 && point2D.x < width && point2D.y < height;
    }
}
