package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteAuthDataTest {
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Delete Auth Data - Positive Test")
    void deleteAuthDataSuccessTest() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        authDataAccess.deleteAuthData(authToken);
        AuthData deletedAuthData = authDataAccess.findAuthData(authToken);
        assertNull(deletedAuthData);
    }

    @Test
    @DisplayName("Delete Auth Data - Negative Test")
    void deleteAuthDataFailureTest() throws DataAccessException {
        String invalidAuthToken = "badAuthToken";
        authDataAccess.deleteAuthData(invalidAuthToken);
        assertTrue(authDataAccess.emptyAuthData());
    }


}
