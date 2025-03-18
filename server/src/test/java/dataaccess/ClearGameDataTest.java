package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearGameDataTest {
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() throws DataAccessException {
        String gameName = "Clear Game Test";
        gameDataAccess.createGameData(gameName);
        gameDataAccess.clearGameData();
        assertTrue(gameDataAccess.emptyGameData());
    }

}
