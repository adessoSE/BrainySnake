package de.adesso.brainysnake.Gamelogic.Player;

import com.badlogic.gdx.graphics.Color;

import de.adesso.brainysnake.Config;
import de.adesso.brainysnake.Gamelogic.Entities.GameObject;
import de.adesso.brainysnake.playercommon.math.Point2D;

public class Snake {

    private GameObject head;

    private Color headColor;

    private GameObject body;

    private Color bodyColor;

    public Snake(GameObject head, GameObject body) {
        this.head = head;
        this.body = body;

        headColor = head.getColor();
        bodyColor = body.getColor();
    }

    public Point2D getHeadPosition() {
        return head.getPositions().get(0);
    }

    private void setHead(Point2D head) {
        this.head.getPositions().set(0, head);
    }

    public void setNextPosition(Point2D nextPosition) {
        body.getPositions().removeLast();
        body.getPositions().addFirst(getHeadPosition());
        setHead(nextPosition);
    }

    public void removeTail() {
        body.getPositions().removeLast();
    }

    public void setGhostMode() {
        head.getColor().a *= Config.GHOSTMODE_OPACITY;
        body.getColor().a *= Config.GHOSTMODE_OPACITY;
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
        headColor = color;
        bodyColor = new Color(color);
        bodyColor.a = Config.SNAKE_BODY_LIGHTING;
    }

    public GameObject getHead() {
        return head;
    }

    public GameObject getBody() {
        return body;
    }

    public int countPoints(){
        return head.getPositions().size() + body.getPositions().size();
    }
}
