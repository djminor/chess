package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.request.*;
import service.result.*;

import java.util.*;

public class UserService {

    UserDataAccess userDataAccess = new MySQLUserDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        if (userDataAccess.findUser(registerRequest.username()) != null) {
            return new RegisterResult("Error: Already taken", "");
        }
        else {
            String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
            UserData user = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());
            userDataAccess.addUser(user);
            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(registerRequest.username(), authToken);
            authDataAccess.addAuth(authData);
            return new RegisterResult(registerRequest.username(), authToken);
        }
    }
    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        if(userDataAccess.findUser(loginRequest.username()) == null) {
            return new LoginResult("Error: Unauthorized", "");
        }
        else {
            if(!verifyPassword(loginRequest.username(), loginRequest.password())) {
                return new LoginResult("Error: Unauthorized", "");
            }
            else {
                String authToken = UUID.randomUUID().toString();
                AuthData authData = new AuthData(loginRequest.username(), authToken);
                authDataAccess.addAuth(authData);
                return new LoginResult(loginRequest.username(), authToken);
            }
        }
    }
    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        if (authDataAccess.findAuthData(logoutRequest.authToken()) != null) {
            authDataAccess.deleteAuthData(logoutRequest.authToken());
            return new LogoutResult(logoutRequest.authToken());
        }
        else {
            return new LogoutResult("Error: bad request");
        }
    }

    private Boolean verifyPassword(String username, String password) throws DataAccessException {
        var hashedPassword = userDataAccess.findUser(username).password();
        return BCrypt.checkpw(password, hashedPassword);
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        if (authDataAccess.findAuthData(listGamesRequest.authToken()) != null) {
            List<GameData> games = gameDataAccess.getGames();
            return new ListGamesResult.Success(games);
        }
        else return new ListGamesResult.Error("Error: Unauthorized");
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        if (authDataAccess.findAuthData(createGameRequest.authToken()) != null) {
            GameData game = gameDataAccess.createGameData(createGameRequest.gameName());
            return new CreateGameResult.Success(game.gameID());
        } else return new CreateGameResult.Error("Error: bad request");
    }

    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String authToken = joinGameRequest.authToken();
        if (authDataAccess.findAuthData(joinGameRequest.authToken()) != null) {
            String playerColor = joinGameRequest.playerColor();
            int gameID = joinGameRequest.gameID();
            return new JoinGameResult(gameDataAccess.setGameData(playerColor, gameID, authToken));
        }
        return new JoinGameResult("Error");
    }

    public ClearDBResult clearDB() throws DataAccessException {
        clearDataAccess.clearDatabase();
        if (clearDataAccess.databaseCleared()) {
            return new ClearDBResult(true);
        }
        else return new ClearDBResult(false);
    }


}
