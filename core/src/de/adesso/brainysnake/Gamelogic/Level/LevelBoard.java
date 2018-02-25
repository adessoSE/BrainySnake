package de.adesso.brainysnake.Gamelogic.Level;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.level.LevelObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * todo rukl doc
 * todo dont us levelobject in gamelocik package. it should be fully separated
 */
public class LevelBoard {

    private static final Logger LOGGER = LoggerFactory.getLogger(LevelBoard.class.getName());

    private final int width, height;

    private LevelObject walls;

    private LevelObject barriers;

    private LevelObject points;

    private LinkedList<Point2D> spawnPositions = new LinkedList<>();

    private LinkedList<Point2D> freeFields;

    public LevelBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.walls = new LevelObject(buildOuterWalls(), Config.LEVEL_COLOR);
        barriers = new LevelObject(buildBarriers(), Config.BARRIER_COLOR);
        points = new LevelObject(null, Config.POINT_COLLOR);
    }

    /**
     * @return a LinkedList with the positions of the generated LevelWalls
     */
    private LinkedList<Point2D> buildOuterWalls() {
        LinkedList<Point2D> positionts = new LinkedList<>();
        for (int x = 0; x < width; x++) {
            // wall top
            positionts.add(new Point2D(x, 0));
            // wall bottom
            positionts.add(new Point2D(x, height - 1));
        }

        for (int y = 1; y < height; y++) {
            // wall left
            positionts.add(new Point2D(0, y));
            // wall right
            positionts.add(new Point2D(width - 1, y));
        }

        return positionts;
    }

    /**
     * @return a LinkedList with all Positions all Barriers
     */
    private LinkedList<Point2D> buildBarriers() {
        // Create a list that contains every unoccupied field in the game
        freeFields = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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
            duplicate.removeAll(new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots());

            freeFields.removeAll((new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots()));
            barriers.addAll((new Barrier(newBarrierPosition.getX(), newBarrierPosition.getY()).getBarrierDots()));
        }
        return barriers;
    }

    private boolean isConflictingBorder(Point2D point) {
        return (point.getX() > (width - 3) || point.getX() < 2 || point.getY() > (height - 3) || point.getY() < 2);
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
     * Creates a new Snake with initialLength, color and random {@link Orientation}
     *
     * @return new snake
     */
    public Snake createStartingGameObject(int initialLength, Color color) {
        LinkedList<Point2D> head = new LinkedList<>();
        LinkedList<Point2D> body = new LinkedList<>();

        Point2D start = getRandomStart(initialLength);
        Orientation orientation = getRandomOrientation();
        int centerX = start.getX();
        int centerY = start.getY();

        head.add(start);
        for (int i = 1; i < initialLength; i++) {
            Point2D positionIn = getPositionIn(orientation, centerX, centerY, i);
            body.add(positionIn);
            LOGGER.info("Snake body spawns at X:{}/Y:{} - Orientation: {}", positionIn.getX(), positionIn.getY(), orientation);
        }
        spawnPositions.addAll(head);
        spawnPositions.addAll(body);
        return new Snake(new LevelObject(head), new LevelObject(body), orientation, color);
    }

    /**
     * Generates a new random Orientation.
     *
     * @return a new random Orientation
     */
    public Orientation getRandomOrientation() {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        switch (randomNumber) {
            case 0:
                return Orientation.UP;
            case 1:
                return Orientation.DOWN;
            case 2:
                return Orientation.LEFT;
            case 3:
                return Orientation.RIGHT;
            default:
                return Orientation.RIGHT;
        }
    }

    /**
     * to get a random start point for a snake with enough space
     *
     * @return random Start with enough space for a snake
     */
    private Point2D getRandomStart(int length) {
        Point2D randomPoint;
        do {
            randomPoint = getRandomLevelPosition();
        }
        //while there is not enough space
        while (!isEnoughSpace(randomPoint, length));
        return randomPoint;
    }

    /**
     * to test if there is enough space for a snake
     * checks if there's nothing in a radius of length around the snake
     *
     * @return true if there's enough space
     */
    private boolean isEnoughSpace(Point2D position, int snakeLength) {

        int snakeHeadx = position.getX();
        int snakeHeady = position.getY();
        snakeLength++;
        try {
            for (int x = snakeHeadx - snakeLength; x < snakeHeadx + snakeLength; x++) {
                for (int y = snakeHeady - snakeLength; y < snakeHeady + snakeLength; y++) {
                    if (checkCollision(new Point2D(x, y)) || spawnPositions.contains(new Point2D(x, y))) {
                        LOGGER.error("Colission at - X:" + x + " Y:" + y);
                        return false;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException");
            return false;
        }
        LOGGER.info("Valid position at - X:" + snakeHeadx + " Y:" + snakeHeady);
        return true;
    }

    /**
     * Checks if the given point collides with an already existing point
     *
     * @return a boolean if the given Point collides with a Barrier or a Wall
     */
    public boolean checkCollision(Point2D point2D) {
        return walls.getPositions().contains(point2D) || barriers.getPositions().contains(point2D);
    }

    /**
     * Generates a position behind the given position depending on the Orientation and the given length.
     *
     * @return new Point
     */
    private Point2D getPositionIn(Orientation orientation, int centerX, int centerY, int length) {
        switch (orientation) {
            case UP:
                return new Point2D(centerX, centerY - length);
            case DOWN:
                return new Point2D(centerX, centerY + length);
            case RIGHT:
                return new Point2D(centerX - length, centerY);
            case LEFT:
                return new Point2D(centerX + length, centerY);
            default:
                return null;
        }
    }

    /**
     * creates a random level position
     *
     * @return random Point2D
     */
    private Point2D getRandomLevelPosition() {
        Random random = new Random();
        int randomXPosition = random.nextInt(width);
        int randomYPosition = random.nextInt(height);
        return new Point2D(randomXPosition, randomYPosition);
    }

    /**
     * todo rukl doc
     * spreads the consumable Points
     */
    public void fillUpWithPoints(int quantity) {
        for (int i = 0; i < (quantity - points.size()); i++) {
            Point2D randomLevelPosition = null;
            do {
                randomLevelPosition = getRandomLevelPosition();
            } while (randomLevelPosition == null || checkCollision(randomLevelPosition));
            points.getPositions().add(new Point2D(randomLevelPosition.x, randomLevelPosition.y));
        }
    }

    /**
     * tries to consume and remove the point at the given position
     *
     * @return true if the point is consumable
     */
    public boolean tryConsumePoint(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.equals(position)) {
                return points.getPositions().remove(point2D);
            }
        }

        return false;
    }

    /**
     * checks if the given point is within the level
     *
     * @return true is given point is within the level
     */
    public boolean contains(Point2D point2D) {
        return point2D.x >= 0 && point2D.y >= 0 && point2D.x < width && point2D.y < height;
    }

    /**
     * returns true if the given position is on the same position as a consumablePoint
     *
     * @return boolean
     */
    public boolean isPointOn(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.x == position.x && point2D.y == position.y) {
                return true;
            }
        }

        return false;
    }

    public LevelObject getWalls() {
        return walls;
    }

    public LevelObject getBarriers() {
        return barriers;
    }

    public LevelObject getPoints() {
        return points;
    }
}
