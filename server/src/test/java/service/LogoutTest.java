package service;

import dataaccess.AuthDataAccess;
import dataaccess.ClearDataAccess;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.result.LoginResult;
import service.result.LogoutResult;


public class LogoutTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Logout - Positive Test")
    void logoutSuccess() {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserDataAccess.addUser(username, password, email);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = UserService.login(loginRequest);

        LogoutRequest logoutRequest = new LogoutRequest(loginResult.authToken());
        UserService.logout(logoutRequest);

        assertNull(AuthDataAccess.findAuthData(loginResult.authToken()));
    }

    @Test
    @DisplayName("Logout - Negative Test")
    void loginFailure() {
        LogoutRequest logoutRequest = new LogoutRequest("nonExistentAuth");
        LogoutResult logoutResult = UserService.logout(logoutRequest);

        assertNotNull(logoutResult);
        assertEquals("Error: bad request", logoutResult.authToken());

    }
}


