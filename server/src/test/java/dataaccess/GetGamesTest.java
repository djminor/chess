package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class GetGamesTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("List Games - Positive Test")
    void getGamesSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "List Games Test";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        var result = gameDataAccess.getGames();
        assertEquals(1, (result.size()));
    }

    @Test
    @DisplayName("List Games - Negative Test")
    void getGamesFailure() throws DataAccessException {
        var result = gameDataAccess.getGames();
        assertEquals(0, result.size());
    }
}
