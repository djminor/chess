package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDataAccess implements GameDataAccess {
    private static final List<GameData> games = new ArrayList<>();

    public List<GameData> getGames() {
        return games;
    }

    public GameData createGameData(String gameName) {
        GameData newGame = new GameData(games.size() + 1, null, null, gameName);
        games.add(newGame);
        return newGame;
    }

    public String setGameData(String playerColor, int gameID, String authToken) throws DataAccessException {
        AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
        String username = authDataAccess.findAuthData(authToken).username();
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

    public void clearGameData() {
        games.clear();
    }

    public Boolean emptyGameData() {
        return games.isEmpty();
    }
}


