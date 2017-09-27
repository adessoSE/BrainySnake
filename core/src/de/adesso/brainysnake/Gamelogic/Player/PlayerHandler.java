package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.playercommon.PlayerState;


public class PlayerHandler {

    private BrainySnakePlayer brainySnakePlayer;
    private Orientation orientation;
    private GameObject gameObject;

    public PlayerHandler(BrainySnakePlayer brainySnakePlayer, Orientation orientation, GameObject gameObject) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.orientation = orientation;
        this.gameObject = gameObject;
    }

    public void sendPlayerState(PlayerState playerState) {
        this.brainySnakePlayer.handlePlayerStatusUpdate(playerState);
    }

    public void requestPlayerUpdate() {
        this.brainySnakePlayer.tellPlayerUpdate();
    }
}
