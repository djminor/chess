package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddAuthTest {
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    ClearDataAccess clearDataAccess = new ClearDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Add Auth - Positive Test")
    void addAuthSuccessTest() throws DataAccessException {
        String username = "authorizedUser";
        String authToken = "1234ABC";
        AuthData authData = new AuthData(username, authToken);
        authDataAccess.addAuth(authData);
        AuthData addedData = authDataAccess.findAuthData(authToken);
        assertNotNull(addedData);
    }

    @Test
    @DisplayName("Add Auth - Negative Test")
    void addAuthFailureTest() {
        String username = "authorizedUser";
        AuthData authData = new AuthData(username, null);
        assertThrows(RuntimeException.class, () -> authDataAccess.addAuth(authData));
    }
}
