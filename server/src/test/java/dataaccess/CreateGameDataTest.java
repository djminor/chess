package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.CreateGameRequest;
import service.result.CreateGameResult;

public class CreateGameDataTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

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
    @DisplayName("Create Game - Negative Test (Empty Game Name)")
    void createGameFailureEmptyName() throws DataAccessException {
        String emptyGameName = "";

        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDataAccess.createGameData(emptyGameName);
        });

        assertEquals("Game name cannot be empty.", exception.getMessage());
    }
}

