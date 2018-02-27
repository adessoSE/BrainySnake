package de.adesso.brainysnake;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
    public static final int DOT_SIZE = 10;
    public static final int APPLICATION_WIDTH = 1280;
    public static final int APPLICATION_HEIGHT = 900;

    /* gameplay settings */
    public static final int MAX_ROUNDS = 1000;
    public static final boolean RENDER_PLAYERVIEW = true;
    public static final int PLAYERVIEW_OFFSET_TO_LEFT = 2;
    public static final int PLAYERVIEW_OFFSET_TO_VIEWWIDTH = PLAYERVIEW_OFFSET_TO_LEFT * 2 + 1;
    public static final int PLAYERVIEW_OFFSET_TO_AHEAD = 5;
    public static final int LEVEL_WIDTH = APPLICATION_WIDTH / DOT_SIZE;
    public static final int LEVEL_HEIGHT = APPLICATION_HEIGHT/ DOT_SIZE;
    public static int RANDOM_PLAYERSPAWN_OFFSET_TO_AHEAD = PLAYERVIEW_OFFSET_TO_AHEAD;
    public static int RANDOM_PLAYERSPAWN_OFFSET_TO_SIDE = PLAYERVIEW_OFFSET_TO_LEFT * 2;

    public static List<Color> PLAYER_COLORS = Arrays.asList(Color.PINK, Color.GREEN,Color.ORANGE, Color.CYAN);

    /* others */
    public static final float UPDATE_RATE = 10f;
    public static final Color POINT_COLLOR = Color.RED;
    public static final Color GHOSTMODE_COLOR = Color.GRAY;
    public static final float SNAKE_BODY_LIGHTING = 0.7f;

    public static final Color LEVEL_COLOR = Color.WHITE;
    public static final Color BARRIER_COLOR = Color.LIGHT_GRAY;
    public static final Color POINT_LABYRINTH_COLOR = Color.LIGHT_GRAY;
    public static final Color COLOR_404 = Color.PINK;
    public static final Color BLINK_COLOR = Color.YELLOW;

    public static final Color DEAD_COLOR = Color.VIOLET;
    public static final int QUANTITY_BARRIERS = 5;
    // If there's not enough space to create each barrier with enough distance to the next barriers, not all barriers are created
    public static final int DISTANCE_BETWEEN_BARRIERS = 10;
    public static final int INITIAL_PLAYER_LENGTH = 10;
    public static final int MAX_POINTS_IN_LEVEL = 40;
    public static final int GHOST_TIME = 10;
    public static final int BLINK_TIME = ((int) UPDATE_RATE / 10) + 1;

    /* threaded playerupdate config */
    public static final int MAX_AGENT_PROCESSING_TIME_MS = 250;
}
