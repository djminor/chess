package dataaccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAuthDataAccess implements AuthDataAccess {
    private Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }
    public AuthData findAuthData(String authToken) throws DataAccessException {
        try (Connection connection = getConnection()) {
            var statement = "SELECT authToken, json from WHERE authToken=?";
            try (var preppedStatement = connection.prepareStatement(statement)) {
                preppedStatement.setString(1, authToken);
                try (var result = preppedStatement.executeQuery()) {
                    if (result.next()) {
                        return new AuthData(
                                result.getString("username"),
                                result.getString("authToken")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    public void addAuth(AuthData auth) {
        String statement = "INSERT INTO authorization (username, authToke, json) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement)) {

            var json = new Gson().toJson(auth);

            preppedStatement.setString(1, auth.username());
            preppedStatement.setString(2, auth.authToken());
            preppedStatement.setString(3, json);

            int inserted = preppedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void clearAuthData() {
        String statement = "DELETE FROM authorization";
        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement)) {
            preppedStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public Boolean emptyAuthData() {
        String statement = "SELECT COUNT(*) FROM authorization";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement);
             var result = preppedStatement.executeQuery()) {

            if (result.next()) {
                return result.getInt(1) == 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authorization (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
