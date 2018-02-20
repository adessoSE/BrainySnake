package de.adesso.brainysnake.Gamelogic;

import com.badlogic.gdx.graphics.Color;
import de.adesso.brainysnake.Gamelogic.Player.TestPlayer.KeyBoardPlayer;
import de.adesso.brainysnake.playercommon.BrainySnakePlayer;
import de.adesso.brainysnake.sampleplayer.SamplePlayer;
import de.adesso.brainysnake.sampleplayer.YourPlayer;
import de.adesso.brainysnake.screenmanagement.screens.PlayerDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serves as a repository of business meta-information of a game process. This includes,
 * for example, which players belong to a match, how many points they have, and there current State in the game.
 */
public class GameBoard {

    private static final GameBoard INSTANCE = new GameBoard();

    // Add all Agents here
    private Map<Color, BrainySnakePlayer> brainySnakePlayers;

    private GameBoard() {
        List<BrainySnakePlayer> playerlist = createBrainySnakePlayerList();
        brainySnakePlayers = createPlayerMap(playerlist, Utils.getShuffledPlayerColor(playerlist.size()));
    }

    public static GameBoard getINSTANCE() {
        return INSTANCE;
    }

    private List<BrainySnakePlayer> createBrainySnakePlayerList() {

        //crate BrainySnakePlayer manualy
        BrainySnakePlayer playerOne = new KeyBoardPlayer();
        BrainySnakePlayer yourPlayer = new YourPlayer();
        BrainySnakePlayer playerTwo = new SamplePlayer() {
            @Override
            public String getPlayerName() {
                return "SamplePlayer Two";
            }
        };
        BrainySnakePlayer playerThree = new SamplePlayer() {

            @Override
            public String getPlayerName() {
                return "SamplePlayer Three";
            }
        };
        BrainySnakePlayer playerFour = new SamplePlayer() {

            @Override
            public String getPlayerName() {
                return "SamplePlayer Four";
            }
        };

        // Add agents to the game
        List<BrainySnakePlayer> playerList = new ArrayList<>();
        playerList.add(playerOne);
        playerList.add(playerTwo);
        playerList.add(playerFour);

        return playerList;
    }

    /**
     * Splits a list of colors on the players.
     *
     * @param brainySnakePlayers List of players
     * @param playerColors       A list of colors to be shared among the players
     * @return Map of {@link BrainySnakePlayer} mapped to Color of Player {@link Color}
     */
    //TODO rukl TESTS
    Map<Color, BrainySnakePlayer> createPlayerMap(List<BrainySnakePlayer> brainySnakePlayers, List<Color> playerColors) {

        if (brainySnakePlayers.size() > playerColors.size()) {
            throw new IllegalStateException("Not enough Playercolors available");
        }

        Map<Color, BrainySnakePlayer> playerMap = new HashMap<>();
        for (int i = 0; i < brainySnakePlayers.size(); i++) {
            playerMap.put(playerColors.get(i), brainySnakePlayers.get(i));
        }

        return playerMap;
    }

    //TODO rukl rename and implement
    public List<PlayerDTO> getPlayerDTO(){

        List<PlayerDTO> test = new ArrayList<>();
        test.add(new PlayerDTO("asdf 1", Color.BLUE, 5));
        test.add(new PlayerDTO("asdf 1", Color.YELLOW, 5));
        test.add(new PlayerDTO("asdf 1", Color.CYAN, 7));
        test.add(new PlayerDTO("asdf 1", Color.ORANGE, 2));

        return test;
    }

    public Map<Color, BrainySnakePlayer> getBrainySnakePlayers() {
        return brainySnakePlayers;
    }
}
