package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUserDataAccess implements UserDataAccess {

    private Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }

    public UserData findUser(String username) throws DataAccessException {
        try (Connection connection = getConnection()) {
            var statement = "SELECT username, json from WHERE username=?";
            try (var preppedStatement = connection.prepareStatement(statement)) {
                preppedStatement.setString(1, username);
                try (var result = preppedStatement.executeQuery()) {
                    if (result.next()) {
                        return new UserData(
                                result.getString("username"),
                                result.getString("password"),
                                result.getString("email")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void addUser(UserData user) {
        String statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             var preppedStatement = connection.prepareStatement(statement)) {

            var json = new Gson().toJson(user);

            preppedStatement.setString(1, user.username());
            preppedStatement.setString(2, user.password());
            preppedStatement.setString(3, user.email());
            preppedStatement.setString(4, json);

            int inserted = preppedStatement.executeUpdate();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void clearUserData() {
        String statement = "DELETE FROM user";
        try (Connection connection = getConnection();
            var preppedStatement = connection.prepareStatement(statement)) {
            preppedStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean emptyUserData() {
        String statement = "SELECT COUNT(*) FROM user";

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
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

}
