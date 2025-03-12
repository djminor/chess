package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDataAccess {

    List<GameData> getGames() throws DataAccessException;

    GameData createGameData(String gameName) throws DataAccessException;

    String setGameData(String playerColor, int gameID, String authToken) throws DataAccessException;

    void clearGameData() throws DataAccessException;

    Boolean emptyGameData() throws DataAccessException;
}

