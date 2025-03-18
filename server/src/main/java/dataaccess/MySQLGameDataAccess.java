package dataaccess;

import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MySQLGameDataAccess implements GameDataAccess {

    public MySQLGameDataAccess() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }

    public List<GameData> getGames() throws DataAccessException {
        List<GameData> result = new ArrayList<>();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM game";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement);
             var resultItem = preppedStatement.executeQuery()) {

            while (resultItem.next()) {
                int gameID = resultItem.getInt("gameID");
                String whiteUsername = resultItem.getString("whiteUsername");
                String blackUsername = resultItem.getString("blackUsername");
                String gameName = resultItem.getString("gameName");
                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName);
                result.add(gameData);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public GameData createGameData(String gameName) throws DataAccessException {

        if (gameName == null || gameName.isEmpty()) {
            throw new DataAccessException("Game name cannot be empty.");
        }

        String statement = "INSERT INTO game (gameName) VALUES (?)";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            preppedStatement.setString(1, gameName);
            int affectedRows = preppedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating game failed, no rows affected.");
            }

            try (var result = preppedStatement.getGeneratedKeys()) {
                if (result.next()) {
                    int gameID = result.getInt(1);
                    return new GameData(gameID, null, null, gameName);
                } else {
                    throw new DataAccessException("Creating game failed, no ID obtained.");
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String setGameData(String playerColor, int gameID, String authToken) throws DataAccessException {
        AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
        String username = authDataAccess.findAuthData(authToken).username();

        String checkQuery = "SELECT whiteUsername, blackUsername FROM game WHERE gameID = ?";
        String updateQuery = "";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(checkQuery)) {

            preppedStatement.setInt(1, gameID);
            try (var result = preppedStatement.executeQuery()) {
                if (!result.next()) {
                    return "Game not found";
                }

                String whiteUsername = result.getString("whiteUsername");
                String blackUsername = result.getString("blackUsername");

                if ("WHITE".equalsIgnoreCase(playerColor)) {
                    if (!Objects.equals(whiteUsername, "") && whiteUsername != null) {
                        return "Steal";
                    }
                    if (Objects.equals(blackUsername, "")) {
                        updateQuery = "UPDATE game SET whiteUsername = ?, blackUsername = NULL WHERE gameID = ?";
                    } else {
                        updateQuery = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
                    }
                } else if ("BLACK".equalsIgnoreCase(playerColor)) {
                    if (!Objects.equals(blackUsername, "") && blackUsername != null) {
                        return "Steal";
                    }
                    if (Objects.equals(whiteUsername, "")) {
                        updateQuery = "UPDATE game SET blackUsername = ?, whiteUsername = NULL WHERE gameID = ?";
                    } else {
                        updateQuery = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
                    }
                } else if (playerColor != null) {
                    return "Invalid color";
                } else {
                    return "Error";
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try (Connection connection = getConnection();
             var updateStatement = connection.prepareStatement(updateQuery)) {

            updateStatement.setString(1, username);
            updateStatement.setInt(2, gameID);
            int affectedRows = updateStatement.executeUpdate();

            return affectedRows > 0 ? "" : "Error updating game";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void clearGameData() {
        System.out.println("Clearing game data...");
        String statement = "TRUNCATE TABLE game";
        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement)) {
            preppedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean emptyGameData() {
        String statement = "SELECT COUNT(*) FROM game";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement);
             var result = preppedStatement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1) == 0;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT '',
              `blackUsername` varchar(256) DEFAULT '',
              `gameName` varchar(256) NOT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
