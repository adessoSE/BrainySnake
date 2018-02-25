package de.adesso.brainysnake.playercommon;

/**
 * Spezifikation des Objekttyps eines Feldes
 */
public enum FieldType {
    /* level Element*/
    LEVEL,

    /* Kein Objekt*/
    EMPTY,

    /* Ein Spieler (kann auch der Spieler selbst sein)*/
    PLAYER,

    /* Konsumierbarer Punkt*/
    POINT,

    /* Null, also außerhalb des Levels*/
    NONE,
}
