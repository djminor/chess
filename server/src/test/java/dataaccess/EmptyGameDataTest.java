package dataaccess;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyGameDataTest {

    ClearDataAccess clearDataAccess = new ClearDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Empty Game Data - Positive Test")
    void emptyGameDataTableTest() throws DataAccessException {
        boolean isEmpty = gameDataAccess.emptyGameData();
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Empty Game Data - Negative Test")
    void nonEmptyGameDataTableTest() throws DataAccessException {
        String gameName = "Test Game";
        gameDataAccess.createGameData(gameName);
        boolean isEmpty = gameDataAccess.emptyGameData();
        assertFalse(isEmpty);
    }
}

