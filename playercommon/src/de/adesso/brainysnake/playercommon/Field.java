package de.adesso.brainysnake.playercommon;

import de.adesso.brainysnake.playercommon.math.Point2D;

public class Field {

    private final Point2D position;
    private final FieldType fieldType;

    public Field(Point2D position, FieldType fieldType) {
        this.position = position;
        this.fieldType = fieldType;
    }

    public Point2D getPosition() {
        return position;
    }

    public FieldType getFieldType() {
        return fieldType;
    }
}
