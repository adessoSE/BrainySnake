package de.adesso.brainysnake.gamelogic.player;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;
import de.adesso.brainysnake.renderer.level.LevelObject;

import java.util.LinkedList;

public class Snake {

    private LevelObject head;

    private Color headColor;

    private LevelObject body;

    private Color bodyColor;

    private Orientation startOrientation;

    public Snake(LevelObject head, LevelObject body, Orientation orientation, Color color) {
        this.head = head;
        this.body = body;
        startOrientation = orientation;
        setHeadAndBodyColor(color);
    }

    /**
     * Sets the color of the snake. The head gets exactly the desired color.
     * The body is set lighter at Config.SNAKE_BODY_LIGHTING
     *
     * @param color
     */
    public void setHeadAndBodyColor(Color color) {
        headColor = new Color(color);
        bodyColor = new Color(color);
        bodyColor.a = Config.SNAKE_BODY_LIGHTING;

        head.setColor(headColor);
        body.setColor(bodyColor);
    }

    public Point2D getHeadPosition() {
        return head.getPositions().get(0);
    }

    public Point2D getTailPosition() {
        return body.getPositions().get(0);
    }

    public void setNextPosition(Point2D nextPosition) {
        body.getPositions().addFirst(getHeadPosition());
        setHead(nextPosition);
    }

    public void removeHead() {
        if (!head.getPositions().isEmpty()) {
            head.getPositions().removeLast();
        }
    }

    public void removeTail() {
        if (!body.getPositions().isEmpty()) {
            body.getPositions().removeLast();
        }
    }

    public void clear(){
        head.getPositions().clear();
        body.getPositions().clear();
    }

    public void setGhostMode() {
        head.setColor(Config.GHOSTMODE_COLOR);
        body.setColor(Config.GHOSTMODE_COLOR);
    }

    public void blink() {
        body.setColor(Config.BLINK_COLOR);
    }

    public void reset() {
        body.setColor(bodyColor);
        head.setColor(headColor);
    }

    public boolean containsPosition(Point2D position) {
        return getHeadPosition().equals(position) || body.getPositions().contains(position);
    }

    public LevelObject getHead() {
        return head;
    }

    private void setHead(Point2D head) {
        this.head.getPositions().set(0, head);
    }

    public LevelObject getBody() {
        return body;
    }

    public Orientation getStartOrientation() {
        return startOrientation;
    }

    public int countPoints() {
        return head.getPositions().size() + body.getPositions().size();
    }

    public LinkedList<Point2D> getAllSnakePositions() {
        LinkedList<Point2D> positions = new LinkedList<Point2D>();
        positions.addAll(head.getPositions());
        positions.addAll(body.getPositions());
        return positions;
    }

    public Color getHeadColor() {
        return headColor;
    }
}
