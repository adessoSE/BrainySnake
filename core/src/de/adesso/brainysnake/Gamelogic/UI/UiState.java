package de.adesso.brainysnake.Gamelogic.UI;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

public final class UiState {

    private static UiState INSTANCE = null;

    private HashMap<String, Color> playerMap = new HashMap<String, Color>();

    private UiState() {
    }

    public static UiState getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new UiState();
        }

        return INSTANCE;
    }

    public void updatePlayerPoints(String playerName, Color color) {
        playerMap.put(playerName, color);
    }

    public void rip(String playerName) {
        playerMap.put(playerName, Color.DARK_GRAY);
    }

    public HashMap<String, Color> getPlayerMap() {
        return playerMap;
    }
}
