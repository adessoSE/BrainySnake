package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;


public class AgentPlayer {

    private BrainySnakePlayer brainySnakePlayer;
    private Orientation orientation;
    private GameObject gameObject;

    public AgentPlayer(BrainySnakePlayer brainySnakePlayer, Orientation orientation, GameObject gameObject) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.orientation = orientation;
        this.gameObject = gameObject;
    }
}
