package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.*;

/**
 * Example implementation of an agent
 */
public class SuperKi implements BrainySnakePlayer {

    private PlayerState playerState;
    private PlayerView playerView;

    boolean afterStepsRight = false;
    boolean afterStepsLeft = false;
    boolean turnedLeft = false;
    boolean turnedRight = false;
    boolean turnAround = false;
    int steps = 0;
    int pointPosition;


    @Override
    public String getPlayerName() {
        return "SampleKI";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        // Processing of the new  PlayerStatusUpdate. Storing the included PlayerState and the PlayerView.
        this.playerState = playerState;
        this.playerView = playerState.getPlayerView();

        // Return value "true" confirms the completed execution.
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        // Storing of the current orientation of the snake. Declaration of the new orientation, if the value doesn't get changed the current orientation should be keeped up.
        Orientation currentOrientation = playerView.getCurrentOrientation();
        Orientation nextStep = currentOrientation;
        if (steps > 0) {
            steps--;
        }
        if (afterStepsRight && steps == 0) {
            afterStepsRight = false;
            nextStep = turnRight(currentOrientation);
        } else if (afterStepsLeft && steps == 0) {
            afterStepsLeft = false;
            nextStep = turnLeft(currentOrientation);
        }

        if (turnAround && turnedLeft && steps == 0) {
            nextStep = turnLeft(currentOrientation);
            turnAround = false;
            turnedLeft = true;
        } else if (turnAround && turnedRight && steps == 0) {
            nextStep = turnRight(currentOrientation);
            turnAround = false;
            turnedRight = true;
        } else if (isLevelAhead()) {
            // The snake turns to the left or right depending where the snake went beforehand, if the snake detects a level object ahead.
            if (turnedRight) {
                nextStep = turnLeft(currentOrientation);
                turnedLeft = true;
                turnAround = true;
                turnedRight = false;
                steps = 2;
            } else {
                nextStep = turnRight(currentOrientation);
                turnedRight = true;
                turnAround = true;
                turnedLeft = false;
                steps = 2;
            }
        } else if (isPointVisible()) {
            // Definition of the behaviour when the snake detects a consumable point in the PlayerView.
            nextStep = getPointDirection(currentOrientation);
            System.out.println(pointPosition);
            pointPosition = -1;
        } else if (isSnakeAhead()) {
            // Definition of the behaviour when the snake detects a snake object ahead. Caution: The snake object could also be part of your own snake!
        }

        // Return of the new PlayerUpdate with the new orientation.
        return new PlayerUpdate(nextStep);
    }

    /**
     * @return Check to see if the field in front of the snake contains a level object
     */
    private boolean isLevelAhead() {
        // Field 22 is the position in front of the head. Further information can be found in the documentation.
        return playerView.getVisibleFields().get(22).getFieldType().equals(FieldType.LEVEL);
    }


    /**
     * Check to see if the field in front of the snake contains a snake object. Caution: The snake object could also be part of your own snake!
     *
     * @return snake is ahead
     */
    private boolean isSnakeAhead() {
        // Field 22 is the position in front of the head. Further information can be found in the documentation.
        return playerView.getVisibleFields().get(22).getFieldType().equals(FieldType.PLAYER);
    }

    /**
     * @return Review of each field in the PlayerView, if it equals a consumable point.
     * Sets the pointPosition.
     */
    private boolean isPointVisible() {
        boolean pointDetected = false;
        pointPosition = -1;
        for (Field visibleField : playerView.getVisibleFields()) {
            pointDetected = visibleField.getFieldType().equals(FieldType.POINT);
            pointPosition++;
            if (pointDetected) {
                break;
            }
        }
        return pointDetected;
    }

    /**
     * @return The path the snake has to take to reach a point visible for the snake.
     */
    private Orientation getPointDirection(Orientation currentOrientation) {

        int p = pointPosition;
        if (p == 2 | p == 7 | p == 12 | p == 17 | p == 22) {
            return currentOrientation;
        } else if (p == 0 | p == 5 | p == 10 | p == 15 | p == 20) {
            steps = 2;
            afterStepsRight = true;
            return turnLeft(currentOrientation);
        } else if (p == 1 | p == 6 | p == 11 | p == 16 | p == 21) {
            steps = 1;
            afterStepsRight = true;
            return turnLeft(currentOrientation);
        } else if (p == 4 | p == 9 | p == 14 | p == 19 | p == 24) {
            steps = 2;
            afterStepsLeft = true;
            return turnRight(currentOrientation);
        } else if (p == 3 | p == 8 | p == 13 | p == 18 | p == 23) {
            steps = 1;
            afterStepsLeft = true;
            return turnRight(currentOrientation);
        } else {
            return currentOrientation;
        }

    }

    private Orientation turnLeft(Orientation currentOrientation) {
        switch (currentOrientation) {
            case UP:
                return Orientation.LEFT;
            case DOWN:
                return Orientation.RIGHT;
            case LEFT:
                return Orientation.DOWN;
            case RIGHT:
                return Orientation.UP;
            default:
                return null;
        }
    }

    private Orientation turnRight(Orientation currentOrientation) {
        switch (currentOrientation) {
            case UP:
                return Orientation.RIGHT;
            case DOWN:
                return Orientation.LEFT;
            case LEFT:
                return Orientation.UP;
            case RIGHT:
                return Orientation.DOWN;
            default:
                return null;
        }
    }
}

