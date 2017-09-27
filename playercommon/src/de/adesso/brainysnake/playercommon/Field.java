package de.adesso.brainysnake.playercommon;

import de.adesso.brainysnake.playercommon.math.Point2D;

public class Field {

    private final Point2D position;
    private final GameObjectType gameObject;

    public Field(Point2D position, GameObjectType gameObject) {
        this.position = position;
        this.gameObject = gameObject;
    }

    public Point2D getPosition() {
        return position;
    }

    public GameObjectType getGameObject() {
        return gameObject;
    }
}
