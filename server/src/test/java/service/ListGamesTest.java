package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.ListGamesRequest;
import service.result.ListGamesResult;

public class ListGamesTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("List Games - Positive Test")
    void listGamesSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "List Games Test";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        var result = userService.listGames(new ListGamesRequest(authToken));
        assertTrue(true);
        assertEquals(1, ((ListGamesResult.Success) result).games().size());
    }

    @Test
    @DisplayName("List Games - Negative Test")
    void listGamesFailure() throws DataAccessException {
        var result = userService.listGames(new ListGamesRequest("invalidToken"));
        assertInstanceOf(ListGamesResult.Error.class, result);
        assertEquals("Error: Unauthorized", ((ListGamesResult.Error) result).errorMessage());
    }
}
