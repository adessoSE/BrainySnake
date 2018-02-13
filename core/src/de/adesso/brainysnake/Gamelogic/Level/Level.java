package de.adesso.brainysnake.Gamelogic.Level;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Game;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class Level {

    private int width, height;

    private GameObject levelWalls;

    private GameObject barriers;

    private GameObject points;

    private LinkedList<Point2D> snakesStartingPositions = new LinkedList<>();

    private int maxPointsInLevel = Config.MAX_POINTS_IN_LEVEL;

    public Level(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        levelWalls = new GameObject(buildOuterWalls(), Config.LEVEL_COLOR);
        barriers = new GameObject(buildBarriers(), Config.BARRIER_COLOR);
        points = new GameObject(null, Config.POINT_COLLOR);
    }

    /**returns a LinkedList with all Positions all Barriers*/
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

    /**returns a LinkedList with the positions of the generated Barrier*/
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

    /**returns a LinkedList with the positions of the generated LevelWalls*/
    private LinkedList<Point2D> buildOuterWalls() {
        LinkedList<Point2D> positionts = new LinkedList<Point2D>();
        for (int x = 0; x < width; x++) {
            // wall top
            positionts.add(new Point2D(x, 0));
            // wall bottom
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
        return levelWalls;
    }

    public GameObject getBarriers() {
        return barriers;
    }

    public GameObject getPoints() {
        return points;
    }

    /**checks if the given point collides with an already existing point*/
    public boolean checkCollision(Point2D point2D) {
        return levelWalls.getPositions().contains(point2D) || barriers.getPositions().contains(point2D);
    }

    /**creates a new Snake and returns the new Snake*/
    public Snake createStartingGameObject(int initialLength) {
        LinkedList<Point2D> head = new LinkedList<Point2D>();
        LinkedList<Point2D> body = new LinkedList<Point2D>();
        Point2D start = getRandomStart(initialLength);
        Orientation orientation = getRandomOrientation();
        int centerX = start.getX();
        int centerY = start.getY();
        for (int i = 1; i <= initialLength; i++) {
            Point2D positionIn = getPositionIn(orientation, centerX, centerY, i);
            if (i == initialLength) {
                head.addFirst(positionIn);
            } else {
                body.addFirst(positionIn);
            }
        }
        snakesStartingPositions.addAll(head);
        snakesStartingPositions.addAll(body);
        return new Snake(new GameObject(head), new GameObject(body), orientation);
    }

    public Orientation getRandomOrientation() {
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        switch(randomNumber){
            case 0 :
                return Orientation.UP;
            case 1 :
                return Orientation.DOWN;
            case 2 :
                return Orientation.LEFT;
            case 3 :
                return Orientation.RIGHT;
            default:
                return Orientation.RIGHT;
        }
    }

    /**returns the next position depending on the Orientation*/
    private Point2D getPositionIn(Orientation orientation, int centerX, int centerY, int length) {
        switch (orientation) {
            case UP:
                return new Point2D(centerX, centerY + length);
            case DOWN:
                return new Point2D(centerX, centerY - length);
            case RIGHT:
                return new Point2D(centerX + length, centerY);
            case LEFT:
                return new Point2D(centerX - length, centerY);
            default:
                return null;
        }
    }

    /**spreads the consumable Points*/
    public void spreadPoints() {
        for (int i = 0; i < (maxPointsInLevel - points.size()); i++) {
            Point2D randomLevelPosition = null;
            do {
                randomLevelPosition = getRandomLevelPosition();
            } while (randomLevelPosition == null || checkCollision(randomLevelPosition));
            points.getPositions().add(new Point2D(randomLevelPosition.x, randomLevelPosition.y));
        }
    }

    /**
     * creates a random level position
     */
    private Point2D getRandomLevelPosition() {
        Random random = new Random();
        int randomXPosition = random.nextInt(width);
        int randomYPosition = random.nextInt(height);
        return new Point2D(randomXPosition, randomYPosition);
    }

    /**
     * tries to consume and remove the a point at the given position
     */
    public boolean tryConsumePoint(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.x == position.x && point2D.y == position.y) {
                return points.getPositions().remove(point2D);
            }
        }

        return false;
    }

    /**
     * returns true if the given position is on the same position as a consumablePoint
     */
    public boolean isPointOn(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.x == position.x && point2D.y == position.y) {
                return true;
            }
        }

        return false;
    }

    /**checks if the given point is within the level*/
    public boolean levelContainsPosition(Point2D point2D) {
        return point2D.x >= 0 && point2D.y >= 0 && point2D.x < width && point2D.y < height;
    }

    /**
     * to test if there is enough space for a snake
     * checks if there's nothing in a radius of length + 1 around the snake
     */
    private boolean isEnoughSpace(Point2D position, int snakeLength) {

        int snakeHeadx = position.getX();
        int snakeHeady = position.getY();
        snakeLength++;
        try {
            for (int x = snakeHeadx - snakeLength; x < snakeHeadx + snakeLength; x++) {
                for (int y = snakeHeady - snakeLength; y < snakeHeady + snakeLength; y++) {
                    if (checkCollision(new Point2D(x, y))&&!snakesStartingPositions.contains(new Point2D(x,y))) {
                        return false;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException");
            return false;
        }
        return true;
    }

    /**
     * to get a random start point for a snake with enough space
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


}
