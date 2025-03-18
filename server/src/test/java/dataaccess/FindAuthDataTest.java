package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FindAuthDataTest {
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Find Auth Data - Positive Test")
    void findAuthDataSuccessTest() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        assertEquals(username, authDataAccess.findAuthData(authToken).username());
        assertEquals(authToken, authDataAccess.findAuthData(authToken).authToken());
    }

    @Test
    @DisplayName("Find Auth Data - Negative Test")
    void findAuthDataFailureTest() throws DataAccessException {
        String authToken = "badAuthToken";
        assertNull(authDataAccess.findAuthData(authToken));
    }
}
