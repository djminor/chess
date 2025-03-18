package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearAuthDataTest {
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        authDataAccess.clearAuthData();
        assertTrue(authDataAccess.emptyAuthData());
    }

}
