package dataaccess;
import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class SetGameDataTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Join Game - Positive Test")
    void setGameDataSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        String gameName = "Join Test";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        gameDataAccess.setGameData("white", 1, authToken);
        assertEquals("authorizedUser", gameDataAccess.getGames().getFirst().whiteUsername());
    }

    @Test
    @DisplayName("Join Game - Negative Test")
    void setGameDataFailure() throws DataAccessException {
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
        gameDataAccess.setGameData("white", 1, authToken1);
        assertEquals("Steal", gameDataAccess.setGameData("white", 1, authToken2));
    }
}

