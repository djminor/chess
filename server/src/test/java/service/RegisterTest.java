package service;

import dataaccess.ClearDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class RegisterTest {

    @BeforeEach
    void setUp() throws DataAccessException {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Register - Positive Test")
    void registerSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";

        UserData user = new UserData(username, password, email);

        UserDataAccess.addUser(user);

        assertNotNull(UserDataAccess.findUser(username));
    }

    @Test
    @DisplayName("Register - Negative Test")
    void registerFailure() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        RegisterRequest request = new RegisterRequest(username, password, email);

        UserService.register(request);

        RegisterResult result = UserService.register(request);

        assertNotNull(result);

        assertEquals("Error: Already taken", result.username());

    }
}

