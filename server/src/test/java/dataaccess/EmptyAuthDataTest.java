package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyAuthDataTest {

    ClearDataAccess clearDataAccess = new ClearDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Empty Game Data - Positive Test")
    void emptyAuthDataTableTest() throws DataAccessException {
        boolean isEmpty = authDataAccess.emptyAuthData();
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Empty Game Data - Negative Test")
    void nonEmptyAuthDataTableTest() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        boolean isEmpty = authDataAccess.emptyAuthData();
        assertFalse(isEmpty);
    }
}


