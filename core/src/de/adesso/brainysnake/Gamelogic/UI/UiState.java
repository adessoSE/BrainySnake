package de.adesso.brainysnake.Gamelogic.UI;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;

public final class UiState {

    private static UiState INSTANCE = null;

    private int roundsRemaining = -1;

    private HashMap<String, UIPlayerInformation> playerMap = new HashMap<String, UIPlayerInformation>();

    private UiState() {
    }

    public static UiState getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new UiState();
        }

        return INSTANCE;
    }

    public void updatePlayerPoints(String playerName, UIPlayerInformation uiPlayerInformation) {
        playerMap.put(playerName, uiPlayerInformation);
    }

    public void rip(String playerName) {
        playerMap.put(playerName, new UIPlayerInformation(Color.GRAY, 0));

    }

    public HashMap<String, UIPlayerInformation> getPlayerMap() {
        return playerMap;
    }

    public String getRoundsRemaining() {
        return Integer.toString(roundsRemaining);
    }

    public void setRoundsRemaining(int roundsRemaining) {
        this.roundsRemaining = roundsRemaining;
    }

}
