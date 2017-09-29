package de.adesso.brainysnake.sampleplayer;

import java.util.List;
import java.util.Random;

import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class SamplePlayer implements BrainySnakePlayer {

    private PlayerState playerState;
    Orientation[] orientations = new Orientation[2];

    @Override
    public String getPlayerName() {
        return "SamplePlayer";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        this.playerState = playerState;
        if(orientations[0] == null) {
            Point2D playersHead = playerState.getPlayersHead();
            Point2D playersTail = playerState.getPlayersTail();
            if((playersHead.x - playersTail.x) == 0) {
                if((playersHead.y - playersTail.y) > 0) {
                    orientations[0] = Orientation.UP;
                } else {
                    orientations[0] = Orientation.DOWN;
                }
            } else {
                if((playersHead.x - playersTail.x) > 0) {
                    orientations[0] = Orientation.RIGHT;
                } else {
                    orientations[0] = Orientation.LEFT;
                }
            }
        }
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        Orientation orientation= chooseNext();
        Point2D nextPosition;

        do {
            nextPosition = this.nextPoint2D(orientation);
        } while (hitWall(nextPosition));

        orientations[0] = orientation;
        return new PlayerUpdate(orientations[0]);
    }

    private Orientation chooseNext() {
        Orientation orientation;
        do {
            orientation = Orientation.values()[(new Random().nextInt(Orientation.values().length))];
        } while (doesInvert(orientation));
        orientations[1] = orientation;

        int rand = (int) (Math.random() * orientations.length);
        return orientations[rand];
    }

    private boolean doesInvert(Orientation orientation) {
        switch (orientations[0]) {
            case DOWN:
                return (orientation != Orientation.UP) ? false : true;
            case UP:
                return (orientation != Orientation.DOWN) ? false : true;
            case RIGHT:
                return (orientation != Orientation.LEFT) ? false : true;
            case LEFT:
                return (orientation != Orientation.RIGHT) ? false : true;
        }
        return true;
    }

    private boolean hitWall(Point2D nextPoint) {
        List<Field> visibleFields = this.playerState.getPlayerView().getVisibleFields();
        for(Field field : visibleFields) {
            if(field.getPosition().equals(nextPoint) && field.getFieldType() == FieldType.LEVEL) {
                return true;
            }
        }
        return false;
    }

    private Point2D nextPoint2D(Orientation orientation) {
        Point2D nextPosition = this.playerState.getPlayersHead().cpy();
        switch (orientation) {
            case UP:
                nextPosition.add(0, 1);
                break;
            case RIGHT:
                nextPosition.add(1, 0);
                break;
            case DOWN:
                nextPosition.add(0, -1);
                break;
            case LEFT:
                nextPosition.add(-1, 0);
                break;
            default:
        }
        return nextPosition;
    }
}
