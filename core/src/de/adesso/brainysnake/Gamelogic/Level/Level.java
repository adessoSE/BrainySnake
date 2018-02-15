package de.adesso.brainysnake.Gamelogic.Level;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    private LinkedList<Point2D> freeFields;

    public Level(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        levelObject = new GameObject(buildOuterWalls(), Config.LEVEL_COLOR);
        barriers = new GameObject(buildBarriers(), Config.BARRIER_COLOR);
        points = new GameObject(null, Config.POINT_COLLOR);
    }

    private LinkedList<Point2D> buildBarriers() {
        // Create a list that contains every unoccupied field in the game
        freeFields = new LinkedList<>();
        for (int i = 0; i < Config.APPLICATION_WIDTH / Config.DOT_SIZE; i++) {
            for (int j = 0; j < Config.APPLICATION_HEIGHT / Config.DOT_SIZE; j++) {
                freeFields.add(new Point2D(i, j));
            }
        }
        // Remove outer walls from list of free fields
        freeFields.removeAll(buildOuterWalls());

        return generateBarriers();
    }

    /**
     * @return a list with all the barriers the level contains
     */
    private LinkedList<Point2D> generateBarriers() {

        LinkedList<Point2D> barriers = new LinkedList<>();

        LinkedList<Point2D> duplicate = (LinkedList) freeFields.clone();

        // Number of barriers to be created, defined in config file.
        for (int x = 0; x < Config.QUANTITY_BARRIERS; x++) {

            Point2D newBarrierPosition = pickBarrierPosition();
            /*
            Make sure barriers don't overlap with level borders or other barriers.
            Remove every conflicting point from duplicate list. If list is empty (i.e. no point fits in the level) stop checking.
            Duplicate list is used so freeFields can still be used to place different level objects.
             */
            while (duplicate.size() > 0 && (isConflictingBarrier(barriers, newBarrierPosition) || isConflictingBorder(newBarrierPosition))) {
                duplicate.removeAll(new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots());
                newBarrierPosition = pickBarrierPosition();
            }
            if (duplicate.size() <= 0) {
                return barriers;
            }
            duplicate.remove(newBarrierPosition);

            freeFields.removeAll((new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots()));
            barriers.addAll((new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots()));
        }
        return barriers;
    }

    /**
     * Generate a random position for the center of a barrier
     *
     * @return returns a random entry from the list of free fields
     */
    private Point2D pickBarrierPosition() {
        int random = ThreadLocalRandom.current().nextInt(0, freeFields.size());
        return freeFields.get(random);
    }

    /**
     * See if new barrier position is too close to existing barrier
     *
     * @param points   the list of existing points in the level
     * @param newPoint the new point that has to be checked against the existing points
     * @return true when there is a conflict between the new point and one of the existing points
     */
    private boolean isConflictingBarrier(List<Point2D> points, Point2D newPoint) {
        for (Point2D point : points) {
            if (!point.equals(newPoint) && point.dst(newPoint) < Config.DISTANCE_BETWEEN_BARRIERS) {
                return true;
            }
        }
        return false;
    }

    private boolean isConflictingBorder(Point2D point) {
        return (point.getX() > (width - 3) || point.getX() < 2 || point.getY() > (height - 3) || point.getY() < 2);
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
