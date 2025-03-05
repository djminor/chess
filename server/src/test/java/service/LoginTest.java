package service;

import dataaccess.ClearDataAccess;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.request.LoginRequest;
import service.result.LoginResult;


public class LoginTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Login - Positive Test")
    void loginSuccess() {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserDataAccess.addUser(username, password, email);
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = UserService.login(request);

        assertNotEquals("", result.authToken());
    }

    @Test
    @DisplayName("Login - Negative Test")
    void loginFailure() {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        LoginRequest request = new LoginRequest(username, password);
        LoginResult result = UserService.login(request);

        assertEquals("", result.authToken());

    }
}

