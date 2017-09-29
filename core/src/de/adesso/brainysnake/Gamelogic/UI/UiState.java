package de.adesso.brainysnake.Gamelogic;

import java.util.HashMap;

public final class UiState {

    private static UiState INSTANCE = null;

    private HashMap<String, Long> playerMap = new HashMap<String, Long>();

    private UiState() {
    }

    public static UiState getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new UiState();
        }

        return INSTANCE;
    }

    public void updatePlayerPoints(String playerName, long points) {
        playerMap.put(playerName, points);
    }

    public void rip(String playerName) {
        playerMap.put(playerName, 0L);
    }
}
