package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.math.GridPoint2;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Level.GlobalGameState;
import de.adesso.brainysnake.Gamelogic.Utils;
import de.adesso.brainysnake.playercommon.*;
import de.adesso.brainysnake.playercommon.PlayerState;
import de.adesso.brainysnake.playercommon.math.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class PlayerHandler {

    private BrainySnakePlayer player;
    private Orientation orientation;
    private GameObject gameObject;
    private PlayerState lastPlayerState;
    private String playerName;

    public PlayerHandler(BrainySnakePlayer player, Orientation orientation, GameObject gameObject) {
        this.player = player;
        this.orientation = orientation;
        this.gameObject = gameObject;
        this.playerName = (player.getPlayerName() == null || player.getPlayerName().isEmpty()) ? UUID.randomUUID().toString() : this.player.getPlayerName().trim();
    }

    public String getPlayerName() {
        return this.playerName;
    }

    /**
     * Calculates the next PlayerState from global GameData
     */
    public void calculatePlayerState(GlobalGameState gameState) {
        // TODO Calc PlayerState from GlobalGameState

        // <Fake Data>
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
        // </Fake Data>
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public PlayerState getLastPlayerState() {
        return lastPlayerState;
    }

    /**
     * Sends the last PlayerState to the Agent
     * Warning: Call this Method from Thread or Test only
     * @return if processing was successful. (This can be ignored)
     */
    Boolean sendPlayerState() {
        return this.player.handlePlayerStatusUpdate(this.lastPlayerState);
    }

    /**
     * Requests the next Update (step) from the Agent (Player)
     * Warning: Call this Method from Thread or Test only
     * @return PlayerUpdate (this can be null
     */
    PlayerUpdate requestPlayerUpdate() {
        return this.player.tellPlayerUpdate();
    }
}
