package service;

import dataaccess.ClearDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataaccess.GameDataAccess;
import dataaccess.AuthDataAccess;
import service.request.CreateGameRequest;
import service.result.CreateGameResult;

public class CreateGameTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Create Game - Positive Test")
    void createGameSuccess() {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "Create Test";
        AuthDataAccess.addAuth(username, authToken);

        GameDataAccess.createGameData(gameName);

        assertEquals("Create Test", GameDataAccess.getGames().getFirst().gameName());
    }

    @Test
    @DisplayName("Create Game - Negative Test")
    void createGameFailure() {
        String gameName = "Create Test";
        String authToken = "1234ABCbadrequest";
        CreateGameResult result = UserService.createGame(new CreateGameRequest(gameName, authToken));

        assertNotNull(result);
        String actualMessage = result.toString().replaceAll("Error\\[error=", "").replaceAll("]", "");
        assertEquals("Error: bad request", actualMessage);


        assertTrue(GameDataAccess.getGames().isEmpty());

    }
}
