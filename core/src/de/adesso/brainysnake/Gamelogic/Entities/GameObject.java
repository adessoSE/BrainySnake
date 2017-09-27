package de.adesso.brainysnake.Gamelogic.Entities;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;

public class GameObject {

    protected List<Dot> dots;

    protected Color color;

    public GameObject() {
        dots = new ArrayList<Dot>();
        color = Config.DEFAULT_PLAYER_COLOR;
    }

    public GameObject(List<Dot> dots, Color color) {
        if (dots == null) {
            this.dots = new ArrayList<Dot>();
        } else {
            this.dots = dots;
        }

        this.color = color;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int size() {
        return dots.size();
    }
}
