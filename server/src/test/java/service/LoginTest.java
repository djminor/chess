package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.LoginRequest;
import service.result.LoginResult;


public class LoginTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    UserService userService = new UserService();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Login - Positive Test")
    void loginSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = userService.login(request);

        assertNotEquals("", result.authToken());
    }

    @Test
    @DisplayName("Login - Negative Test")
    void loginFailure() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = userService.login(request);

        assertEquals("", result.authToken());

    }
}

