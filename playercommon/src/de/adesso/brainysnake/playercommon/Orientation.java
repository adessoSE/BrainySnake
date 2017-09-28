package de.adesso.brainysnake.playercommon;

public enum Orientation {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    private static int size = Orientation.values().length;

    public static int getSize() {
        return size;
    }
}
