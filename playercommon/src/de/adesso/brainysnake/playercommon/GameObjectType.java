package de.adesso.brainysnake.playercommon;

public enum GameObjectType {
    /* Nothing here */
    EMPTY,
    /* You shall not pass! */
    BARRIER,
    /* Let yor snake grow */
    FOOD,
    /* Might be an enemy's snake or your own body */
    SNAKE_BODY,
    /* Be careful */
    SNAKE_HEAD,
    /* Maybe*/
    TRAPDOOR;
}
