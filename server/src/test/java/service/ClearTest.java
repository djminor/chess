package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.result.ClearDBResult;

public class ClearTest {

    @BeforeEach
    void setUp() throws DataAccessException {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() throws DataAccessException {
        String gameName = "Clear Game Test";
        String username = "authorizedUser";
        String password = "1234ABC";
        String email = "hello@world.com";
        UserData user = new UserData(username, password, email);
        UserDataAccess.addUser(user);
        AuthDataAccess.addAuth("authorizedUser", "randomnumbers");
        GameDataAccess.createGameData(gameName);

        ClearDBResult result = UserService.clearDB();

        assertTrue(result.success());
        assertEquals(Boolean.TRUE, UserDataAccess.emptyUserData());
        assertTrue(AuthDataAccess.emptyAuthData());
        assertTrue(GameDataAccess.emptyGameData());
    }
}

