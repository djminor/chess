package service;

import dataaccess.ClearDataAccess;
import dataaccess.UserDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class RegisterTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Register - Positive Test")
    void registerSuccess() {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";

        UserDataAccess.addUser(username, password, email);

        assertNotNull(UserDataAccess.findUser(username));
    }

    @Test
    @DisplayName("Register - Negative Test")
    void registerFailure() {
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

