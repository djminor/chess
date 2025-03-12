package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.result.ClearDBResult;

public class ClearTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() throws DataAccessException {
        String gameName = "Clear Game Test";
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        userDataAccess.addUser(user);
        authDataAccess.addAuth("authorizedUser", "randomnumbers");
        gameDataAccess.createGameData(gameName);
        ClearDBResult result = userService.clearDB();
        assertTrue(result.success());
        assertEquals(Boolean.TRUE, userDataAccess.emptyUserData());
        assertTrue(authDataAccess.emptyAuthData());
        assertTrue(gameDataAccess.emptyGameData());
    }
}

