package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.*;

/**
 * Example implementation of an agent
 */
public class SamplePlayer implements BrainySnakePlayer {

    private PlayerState playerState;
    private PlayerView playerView;

    @Override
    public String getPlayerName() {
        return "SamplePlayer";
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

        if (isLevelAhead()) {
            // The snake turns to the left, if the snake detects a level object ahead.
            nextStep = turnLeft(currentOrientation);
        } else if (isPointVisible()) {
            // Definition of the behaviour when the snake detects a consumable point in the PlayerView.
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
     */
    private boolean isPointVisible() {
        boolean pointDetected = false;

        for (Field visibleField : playerView.getVisibleFields()) {
            pointDetected = visibleField.getFieldType().equals(FieldType.POINT);
            if (pointDetected){
                break;
            }
        }

        return pointDetected;
    }

    /**
     * Calculation of turing left relative to the current orientation
     *
     * @param currentOrientation
     * @return new Orientation
     */
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
}
