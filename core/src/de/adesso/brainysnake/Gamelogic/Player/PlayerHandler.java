package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Utils;
import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.List;


public class PlayerHandler {

    private BrainySnakePlayer brainySnakePlayer;
    private Orientation orientation;
    private GameObject gameObject;
    private PlayerState lastPlayerState;

    public PlayerHandler(BrainySnakePlayer brainySnakePlayer, Orientation orientation, GameObject gameObject) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.orientation = orientation;
        this.gameObject = gameObject;
    }

    /**
     * Calculates the next PlayerState from global GameData
     */
    public void calculatePlayerState() {
        //
        Point2D head = Utils.fromGridPoint2(new GridPoint2(5,5));
        Point2D tail = Utils.fromGridPoint2(new GridPoint2(5, 1));

        List<Field> fieldList = new ArrayList<Field>();
        for(int x = -3; x <= 3; x++) {
            for(int y = 1; y < 6; y++) {
                fieldList.add(new Field(new Point2D(head.x + x, head.y + y),GameObjectType.EMPTY));
            }
        }


        PlayerView playerView = new PlayerView(fieldList);

        this.lastPlayerState = new PlayerState(100, 200, 5, head, tail, false, GameEvent.MOVED, false, playerView);
    }

    public void sendPlayerState() {
        this.brainySnakePlayer.handlePlayerStatusUpdate(this.lastPlayerState);
    }

    public void requestPlayerUpdate() {
        this.brainySnakePlayer.tellPlayerUpdate();
    }
}
