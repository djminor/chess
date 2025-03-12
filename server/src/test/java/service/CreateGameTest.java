package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.CreateGameRequest;
import service.result.CreateGameResult;

public class CreateGameTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Create Game - Positive Test")
    void createGameSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "Create Test";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        assertEquals("Create Test", gameDataAccess.getGames().getFirst().gameName());
    }

    @Test
    @DisplayName("Create Game - Negative Test")
    void createGameFailure() throws DataAccessException {
        String gameName = "Create Test";
        String authToken = "1234ABCbadrequest";
        CreateGameResult result = userService.createGame(new CreateGameRequest(gameName, authToken));
        assertNotNull(result);
        String actualMessage = result.toString().replaceAll("Error\\[error=", "").replaceAll("]", "");
        assertEquals("Error: bad request", actualMessage);
        assertTrue(gameDataAccess.getGames().isEmpty());
    }
}
