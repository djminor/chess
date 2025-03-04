package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameDataAccess {
    private static final List<GameData> games = new ArrayList<>();

    public static List<GameData> getGames() {
        return games;
    }

    public static GameData createGame(String gameName) {
        GameData newGame = new GameData(games.size() + 1, null, null, gameName);
        games.add(newGame);
        return newGame;
    }

    public static void clearGameData() {
        games.clear();
    }
}

