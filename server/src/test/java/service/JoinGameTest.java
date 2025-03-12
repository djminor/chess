package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.JoinGameRequest;
import service.result.JoinGameResult;

public class JoinGameTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Join Game - Positive Test")
    void joinGameSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "Join Test";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        userService.joinGame(new JoinGameRequest(authToken, "WHITE", 1));
        assertEquals("authorizedUser", gameDataAccess.getGames().getFirst().whiteUsername());
    }

    @Test
    @DisplayName("Join Game - Negative Test")
    void joinGameFailure() throws DataAccessException {
        String player1 = "player 1";
        String player2 = "player 2";
        String authToken1 = "auth1";
        String authToken2 = "auth2";
        String gameName = "Join Test";
        AuthData authData1 = new AuthData(player1, authToken1);
        AuthData authData2 = new AuthData(player2, authToken2);
        authDataAccess.addAuth(authData1);
        authDataAccess.addAuth(authData2);
        gameDataAccess.createGameData(gameName);
        userService.joinGame(new JoinGameRequest(authToken1, "WHITE", 1));
        JoinGameResult result = userService.joinGame(new JoinGameRequest(authToken2, "WHITE", 1));
        assertEquals("Steal", result.errorMessage());
    }
}
