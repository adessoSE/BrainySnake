package de.adesso.brainysnake.Gamelogic.Player;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.playercommon.Orientation;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class Snake {

    private GameObject head;

    private Color headColor;

    private GameObject body;

    private Color bodyColor;

    private Orientation startOrientation;

    public Snake(GameObject head, GameObject body, Orientation orientation) {
        this.head = head;
        this.body = body;
        startOrientation = orientation;
        headColor = head.getColor();
        bodyColor = body.getColor();
    }

    public Point2D getHeadPosition() {
        return head.getPositions().get(0);
    }

    public Point2D getTailPosition() {
        return body.getPositions().get(0);
    }

    private void setHead(Point2D head) {
        this.head.getPositions().set(0, head);
    }

    public void setNextPosition(Point2D nextPosition) {
        body.getPositions().addFirst(getHeadPosition());
        setHead(nextPosition);
    }

    public void removeHead(){
        head.getPositions().removeLast();
    }

    public void removeTail() {
        body.getPositions().removeLast();
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

    public void setColor(Color color) {
        headColor = new Color(color);
        bodyColor = new Color(color);
        bodyColor.a = Config.SNAKE_BODY_LIGHTING;

        head.setColor(headColor);
        body.setColor(bodyColor);
    }

    public GameObject getHead() {
        return head;
    }

    public GameObject getBody() {
        return body;
    }

    public Orientation getStartOrientation(){
        return startOrientation;
    }

    public int countPoints(){
        return head.getPositions().size() + body.getPositions().size();
    }

    public LinkedList<Point2D> getAllSnakePositions(){
        LinkedList<Point2D> positions = new LinkedList<Point2D>();
        positions.addAll(head.getPositions());
        positions.addAll(body.getPositions());
        return positions;
    }

    public Color getHeadColor() {
        return headColor;
    }
}
