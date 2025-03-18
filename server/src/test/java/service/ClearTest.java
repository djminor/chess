package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.result.ClearDBResult;

public class ClearTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MySQLUserDataAccess();
    UserService userService = new UserService();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();

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
        AuthData authData = new AuthData(username, "randomnumbers");
        userDataAccess.addUser(user);
        authDataAccess.addAuth(authData);
        gameDataAccess.createGameData(gameName);
        ClearDBResult result = userService.clearDB();
        assertTrue(result.success());
        assertEquals(Boolean.TRUE, userDataAccess.emptyUserData());
        assertTrue(authDataAccess.emptyAuthData());
        assertTrue(gameDataAccess.emptyGameData());
    }
}

