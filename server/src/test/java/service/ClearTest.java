package service;

import dataaccess.ClearDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.AuthDataAccess;
import dataaccess.GameDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.result.ClearDBResult;

public class ClearTest {

    @BeforeEach
    void setUp() {
        ClearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() {
        String gameName = "Clear Game Test";
        UserDataAccess.addUser("authorizedUser", "1234ABC", "hello@world.com");
        AuthDataAccess.addAuth("authorizedUser", "randomnumbers");
        GameDataAccess.createGameData(gameName);

        ClearDBResult result = UserService.clearDB();

        assertTrue(result.success());
        assertTrue(UserDataAccess.emptyUserData());
        assertTrue(AuthDataAccess.emptyAuthData());
        assertTrue(GameDataAccess.emptyGameData());
    }
}

