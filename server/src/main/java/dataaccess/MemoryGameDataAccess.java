package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoryGameDataAccess implements GameDataAccess {
    private static final List<GameData> GAMES_DATA = new ArrayList<>();

    public List<GameData> getGames() {
        return GAMES_DATA;
    }

    public GameData createGameData(String gameName) {
        GameData newGame = new GameData(GAMES_DATA.size() + 1, null, null, gameName);
        GAMES_DATA.add(newGame);
        return newGame;
    }

    public String setGameData(String playerColor, int gameID, String authToken) throws DataAccessException {
        AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
        String username = authDataAccess.findAuthData(authToken).username();
        if (Objects.equals(playerColor, "WHITE")) {
            GameData oldGame = GAMES_DATA.get(gameID - 1);
            if (oldGame.whiteUsername() != null) {
                return "Steal";
            }
            GameData updatedGame = new GameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName());
            GAMES_DATA.set(gameID - 1, updatedGame);
            return "";
        }
        if (Objects.equals(playerColor, "BLACK")) {
            GameData oldGame = GAMES_DATA.get(gameID - 1);
            if (oldGame.blackUsername() != null) {
                return "Steal";
            }
            GameData updatedGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName());
            GAMES_DATA.set(gameID - 1, updatedGame);
            return "";
        }
        if (playerColor != null) {
            return "Invalid color";
        }
        return "Error";
    }

    public void clearGameData() {
        GAMES_DATA.clear();
    }

    public Boolean emptyGameData() {
        return GAMES_DATA.isEmpty();
    }
}


