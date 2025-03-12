package service;

import dataaccess.ClearDataAccess;
import dataaccess.DataAccessException;
import dataaccess.UserDataAccess;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.LoginRequest;
import service.result.LoginResult;


public class LoginTest {

    @BeforeEach
    void setUp() throws DataAccessException {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Login - Positive Test")
    void loginSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        UserDataAccess.addUser(user);
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = UserService.login(request);

        assertNotEquals("", result.authToken());
    }

    @Test
    @DisplayName("Login - Negative Test")
    void loginFailure() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = UserService.login(request);

        assertEquals("", result.authToken());

    }
}

