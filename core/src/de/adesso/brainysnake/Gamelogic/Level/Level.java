package de.adesso.brainysnake.Gamelogic.Level;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Player.Snake;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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

    /*
    public Snake createStartingGameObject(Orientation orientation, int initialLength) {
        LinkedList<Point2D> head = new LinkedList<Point2D>();
        LinkedList<Point2D> body = new LinkedList<Point2D>();
        Orientation randomOrientation = getRandomOrientation();
        int centerX = (int) Math.floor(this.width / 2D);
        int centerY = (int) Math.floor(this.height / 2D);
        for (int i = 1; i <= initialLength; i++) {
            Point2D positionIn = getPositionIn(randomOrientation, centerX, centerY, i);
            if (i == initialLength) {
                head.addFirst(positionIn);
            } else {
                body.addFirst(positionIn);
            }
        }
      **/
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

        return new Snake(new GameObject(head), new GameObject(body), orientation);
    }

    //get random orientation
    public Orientation getRandomOrientation() {
        int min = 1;
        int max = 100;
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        if (randomNum < 26) {
            System.out.println("UP");
            return Orientation.UP;
        } else if (randomNum < 51) {
            System.out.println("DOWN");
            return Orientation.DOWN;
        } else if (randomNum < 76) {
            System.out.println("LEFT");
            return Orientation.LEFT;
        } else if (randomNum < 101) {
            System.out.println("RIGHT");
            return Orientation.RIGHT;
        } else System.out.println("Kritischer Fehler!");
        return Orientation.RIGHT;
    }

    /*
    public Snake createStartingGameObject(Orientation orientation, int initialLength, boolean random) {
        LinkedList<Point2D> head = new LinkedList<Point2D>();
        LinkedList<Point2D> body = new LinkedList<Point2D>();
        head.addFirst(getRandomStart(initialLength));


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
    **/

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
        Random random = new Random();
        int randomXPosition = random.nextInt(width);
        int randomYPosition = random.nextInt(height);
        return new Point2D(randomXPosition, randomYPosition);
    }
    /*
    private Point2D getRandomLevelPositionOn(){
        Point2D randomPointOn;
        do{randomPointOn = getRandomLevelPosition();}
        while(!isPointOn(randomPointOn));
        return randomPointOn;
    }**/

    /**trys to consume and remove the a point at the given position*/
    public boolean tryConsumePoint(Point2D position) {
        for (Point2D point2D : points.getPositions()) {
            if (point2D.x == position.x && point2D.y == position.y) {
                return points.getPositions().remove(point2D);
            }
        }

        return false;
    }

    /**returns true if the given position is on the same position as a point*/
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

    /**to test if there is enough space for a snake*/
    private boolean isEnoughSpace(Point2D position, int length) {

        int centerx = position.getX();
        int centery = position.getY();
        length++;
        try {
            for (int x = centerx - length; x < centerx + length; x++) {
                for (int y = centery - length; y < centery + length; y++) {
                    if (checkCollision(new Point2D(x, y))) return false;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IndexOutOfBoundsException");
            return false;
        }

        return true;
    }

    //to get a random start point for a snake
    private Point2D getRandomStart(int length) {
        Point2D randomPoint;
        do {
                randomPoint = getRandomLevelPosition();
        }
        //while there is not enough space
        while (!isEnoughSpace(randomPoint, length));
        System.out.println("x:" + randomPoint.getX() + "y:" + randomPoint.getY());
        return randomPoint;
    }


}
