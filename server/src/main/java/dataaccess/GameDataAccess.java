package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameDataAccess {
    private static final List<GameData> games = new ArrayList<>();

    public static List<GameData> getGames() {
        return games;
    }

    public static GameData createGameData(String gameName) {
        GameData newGame = new GameData(games.size() + 1, null, null, gameName);
        games.add(newGame);
        return newGame;
    }

    public static String setGameData(String playerColor, int gameID, String authToken) {
        String username = AuthDataAccess.findAuthData(authToken).username();
        if (Objects.equals(playerColor, "WHITE")) {
            GameData oldGame = games.get(gameID - 1);
            if (oldGame.whiteUsername() != null) {
                return "Steal";
            }
            GameData updatedGame = new GameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName());
            games.set(gameID - 1, updatedGame);
            return "";
        }
        if (Objects.equals(playerColor, "BLACK")) {
            GameData oldGame = games.get(gameID - 1);
            if (oldGame.blackUsername() != null) {
                return "Steal";
            }
            GameData updatedGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName());
            games.set(gameID - 1, updatedGame);
            return "";
        }
        if (playerColor != null) {
            return "Invalid color";
        }
        return "Error";
    }

    public static void clearGameData() {
        games.clear();
    }

    public static Boolean emptyGameData() {
        return games.isEmpty();
    }
}

