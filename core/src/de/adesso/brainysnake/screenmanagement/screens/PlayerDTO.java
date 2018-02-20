package de.adesso.brainysnake.screenmanagement.screens;

import com.badlogic.gdx.graphics.Color;

//TODO
public class PlayerDTO {

    private String name;

    private Color color;

    private long size;

    public PlayerDTO(String name, Color color, long size){
        this.name = name;
        this.color = color;
        this.size = size;
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
}
