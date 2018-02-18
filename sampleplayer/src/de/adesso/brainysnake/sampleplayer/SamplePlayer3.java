package de.adesso.brainysnake.sampleplayer;

import de.adesso.brainysnake.playercommon.*;


/**
 * Beispiel
 */
public class SamplePlayer3 implements BrainySnakePlayer {

    private PlayerState playerState;

    public SamplePlayer3() {
    }

    @Override
    public String getPlayerName() {
        return "SamplePlayer2";
    }

    @Override
    public boolean handlePlayerStatusUpdate(PlayerState playerState) {
        this.playerState = playerState;
        return true;
    }

    @Override
    public PlayerUpdate tellPlayerUpdate() {
        PlayerView playerView = playerState.getPlayerView();

        Orientation myChoice;
        if (isLeveAhead()) {
            myChoice = turnLeft(playerView.getCurrentOrientation());
        }else {
            myChoice = moveForward();
        }
        return new PlayerUpdate(myChoice);
    }

    private Orientation moveForward(){
        return playerState.getPlayerView().getCurrentOrientation();
    }

    private boolean isLeveAhead(){
        Field field = playerState.getPlayerView().getVisibleFields().get(23);
        return field.getFieldType().equals(FieldType.LEVEL);
    }

    private Orientation turnLeft(Orientation currentOrientation) {
        switch (currentOrientation) {
            case UP:
                return Orientation.LEFT;
            case DOWN:
                return Orientation.RIGHT;
            case LEFT:
                return Orientation.UP;
            case RIGHT:
                return Orientation.DOWN;
        }

        return null;
    }

}