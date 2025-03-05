package service;

import dataaccess.ClearDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;
import service.request.JoinGameRequest;
import service.result.JoinGameResult;

public class JoinGameTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Join Game - Positive Test")
    void joinGameSuccess() {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "Join Test";
        AuthDataAccess.addAuth(username, authToken);

        GameDataAccess.createGameData(gameName);

        UserService.joinGame(new JoinGameRequest(authToken, "WHITE", 1));

        assertEquals("authorizedUser", GameDataAccess.getGames().getFirst().whiteUsername());
    }

    @Test
    @DisplayName("Join Game - Negative Test")
    void joinGameFailure() {
        String player1 = "player 1";
        String player2 = "player 2";
        String authToken1 = "auth1";
        String authToken2 = "auth2";
        String gameName = "Join Test";
        AuthDataAccess.addAuth(player1, authToken1);
        AuthDataAccess.addAuth(player2, authToken2);

        GameDataAccess.createGameData(gameName);

        UserService.joinGame(new JoinGameRequest(authToken1, "WHITE", 1));

        JoinGameResult result = UserService.joinGame(new JoinGameRequest(authToken2, "WHITE", 1));

        assertEquals("Steal", result.errorMessage());
    }
}
