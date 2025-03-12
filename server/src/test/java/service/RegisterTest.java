package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.request.RegisterRequest;
import service.result.RegisterResult;

public class RegisterTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    UserService userService = new UserService();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Register - Positive Test")
    void registerSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        assertNotNull(userDataAccess.findUser(username));
    }

    @Test
    @DisplayName("Register - Negative Test")
    void registerFailure() throws DataAccessException {
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        RegisterRequest request = new RegisterRequest(username, password, email);
        userService.register(request);
        RegisterResult result = userService.register(request);
        assertNotNull(result);
        assertEquals("Error: Already taken", result.username());
    }
}

