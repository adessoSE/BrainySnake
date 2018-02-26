package de.adesso.brainysnake.playercommon;

/**
 * Spezifikation des Objekttyps eines Feldes
 */
public enum FieldType {
    /* Ein Wandelemtn des Levels */
    LEVEL,

    /* Kein Objekt, als freies Feld */
    EMPTY,

    /* Ein Spieler/Agent (kann auch der Spieler selbst sein) */
    PLAYER,

    /* Konsumierbarer Punkt */
    POINT,

    /* Null, also außerhalb des Levels */
    NONE,
}
