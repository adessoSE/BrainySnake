package de.adesso.brainysnake.Gamelogic.Level;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;

public class Level {

    private int width;

    private int height;

    private GameObject levelObject;

    private Color color;

    public Level(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
        levelObject = new GameObject(buildOuterWalls(), color);
        buildBarriers();
    }

    private void buildBarriers() {
        //TODO rukl@rukl
    }

    private List<Dot> buildOuterWalls() {
        List<Dot> dots = new ArrayList<Dot>();
        for (int x = 0; x < width; x++) {
            //wall bottom
            dots.add(new Dot(x, 0));
            //wall top
            dots.add(new Dot(x, height-1));
        }

        for (int y = 1; y < width; y++) {
            //wall left
            dots.add(new Dot(0, y));
            //wall right
            dots.add(new Dot(width-1, y));
        }

        return dots;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public GameObject getLevel() {
        return levelObject;
    }

    public boolean checkCollision(int x, int y) {

        //check if doc collides with level element
        for (Dot tempDot : levelObject.getDots()) {
            if (tempDot.getX() == x && tempDot.getY() == y) {
                return true;
            }
        }

        //TODO rukl@rukl check if dot collides with barrier element of level

        return false;
    }

}
