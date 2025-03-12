package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.result.LoginResult;
import service.result.LogoutResult;


public class LogoutTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Logout - Positive Test")
    void logoutSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = userService.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        userService.logout(logoutRequest);
        assertNull(authDataAccess.findAuthData(loginResult.authToken()));
    }

    @Test
    @DisplayName("Logout - Negative Test")
    void loginFailure() throws DataAccessException {
        LogoutRequest logoutRequest = new LogoutRequest("nonExistentAuth");
        LogoutResult logoutResult = userService.logout(logoutRequest);
        assertNotNull(logoutResult);
        assertEquals("Error: bad request", logoutResult.authToken());

    }
}


