package service;

import dataaccess.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.ListGamesRequest;
import service.result.ListGamesResult;

public class ListGamesTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();

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
        authDataAccess.addAuth(username, authToken);
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
