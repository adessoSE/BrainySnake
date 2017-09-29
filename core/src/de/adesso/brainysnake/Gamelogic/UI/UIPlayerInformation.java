package de.adesso.brainysnake.Gamelogic.UI;


import com.badlogic.gdx.graphics.Color;

public class UIPlayerInformation {

    private Color color;

    private int points;

    public UIPlayerInformation(Color color, int points) {
        this.color = color;
        this.points = points;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getPoints() {
        return Integer.toString(points);
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
