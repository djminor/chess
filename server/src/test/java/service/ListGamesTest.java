package service;

import dataaccess.ClearDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;
import service.request.ListGamesRequest;
import service.result.ListGamesResult;

public class ListGamesTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("List Games - Positive Test")
    void listGamesSuccess() {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "List Games Test";
        AuthDataAccess.addAuth(username, authToken);

        GameDataAccess.createGameData(gameName);

        var result = UserService.listGames(new ListGamesRequest(authToken));

        assertTrue(true);
        assertEquals(1, ((ListGamesResult.Success) result).games().size());
    }

    @Test
    @DisplayName("List Games - Negative Test")
    void listGamesFailure() {
        var result = UserService.listGames(new ListGamesRequest("invalidToken"));
        assertInstanceOf(ListGamesResult.Error.class, result);
        assertEquals("Error: Unauthorized", ((ListGamesResult.Error) result).errorMessage());
    }
}
