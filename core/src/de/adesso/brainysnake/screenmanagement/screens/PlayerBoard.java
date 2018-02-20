package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;

/**
 * Business object for the score and game properties of a player
 */
public class PlayerBoard {

    private BrainySnakePlayer brainySnakePlayer;

    private String name;

    private Color color = Config.DEAD_COLOR;

    private long size = 0;

    private boolean isDead = false;

    public PlayerBoard(BrainySnakePlayer brainySnakePlayer) {
        this.brainySnakePlayer = brainySnakePlayer;
        this.name = brainySnakePlayer.getPlayerName();
    }

    public BrainySnakePlayer getBrainySnakePlayer() {
        return brainySnakePlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
