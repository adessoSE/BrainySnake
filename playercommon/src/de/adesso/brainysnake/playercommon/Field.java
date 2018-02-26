package de.adesso.brainysnake.playercommon;

import de.adesso.brainysnake.playercommon.math.Point2D;

/**
 * Enthält Informationen,
 * welches Objekt sich an einer Position im Level befindet. Die Position ist eine zweidimensionale kartesische Koordinate (rechtshändig) im Level vom Typ {@link Point2D}.
 * Der Objekttyp, also welche Art Object an dieser Position enthalten ist, wird über das Enum {@link FieldType} spezifiziert.
 */
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
