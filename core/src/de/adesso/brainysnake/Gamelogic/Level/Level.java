package de.adesso.brainysnake.Gamelogic.Level;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.Dot;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.Gamelogic.Player.Agent;
import de.adesso.brainysnake.Gamelogic.Player.Orientation;

public class Level {

    private int width, height;

    private GameObject levelObject;

    private GameObject barriers;

    private Color color;

    public Level(int height, int width, Color color) {
        this.height = height;
        this.width = width;
        this.color = color;
        levelObject = new GameObject(buildOuterWalls(), color);
        barriers = new GameObject(buildBarriers(), color);
    }

    private List<Dot> buildBarriers() {
        List<Dot> barriers = new ArrayList<Dot>();
        int quantityBarriers = Config.QUANTITY_BARRIERS;
        int gapBarriersX = width / 4;
        int gapBarriersY = height / 3;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                barriers.addAll(addBarrier((x + 1) * gapBarriersX, (y + 1) * gapBarriersY));
            }
        }

        return barriers;
    }

    private List<Dot> addBarrier(int x, int y) {
        List<Dot> barrierDots = new ArrayList<Dot>();

        barrierDots.add(new Dot(x, y +1));
        barrierDots.add(new Dot(x, y));
        barrierDots.add(new Dot(x, y -1));

        barrierDots.add(new Dot(x + 1, y + 1));
        barrierDots.add(new Dot(x + 1, y));
        barrierDots.add(new Dot(x + 1, y - 1));

        barrierDots.add(new Dot(x - 1, y + 1));
        barrierDots.add(new Dot(x - 1, y));
        barrierDots.add(new Dot(x - 1, y - 1));
        return barrierDots;
    }

    private List<Dot> buildOuterWalls() {
        List<Dot> dots = new ArrayList<Dot>();
        for (int x = 0; x < width; x++) {
            //wall bottom
            dots.add(new Dot(x, 0));
            //wall top
            dots.add(new Dot(x, height - 1));
        }

        for (int y = 1; y < width; y++) {
            //wall left
            dots.add(new Dot(0, y));
            //wall right
            dots.add(new Dot(width - 1, y));
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

    public GameObject getBarriers() {
        return barriers;
    }

    public boolean checkCollision(int x, int y) {

        //check if doc collides with level element
        for (Dot tempDot : levelObject.getDots()) {
            if (tempDot.getX() == x && tempDot.getY() == y) {
                return true;
            }
        }

        for (Dot tempDot : barriers.getDots()) {
            if (tempDot.getX() == x && tempDot.getY() == y) {
                return true;
            }
        }

        return false;
    }

    public GameObject createStartingGameObject(Orientation orientation, int initialLength, Color color) {
        List<Dot> elements= new ArrayList<Dot>();

        int centerX = (int) Math.floor(this.width / 2D);
        int centerY = (int) Math.floor(this.height / 2D);

        for(int i = 0; i <= initialLength; i++) {
            switch (orientation) {
                case UP:
                    elements.add(new Dot(centerX + 1, centerY + i + 1));
                    break;
                case DOWN:
                    elements.add(new Dot(centerX - 1, centerY - i - 1));
                    break;
                case RIGHT:
                    elements.add(new Dot(centerX + i + 1, centerY + 1));
                    break;
                case LEFT:
                    elements.add(new Dot(centerX - i - 1, centerY - 1));
                    break;
            }
        }
        return new GameObject(elements, color);
    }

}
