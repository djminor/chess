package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearUserDataTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MySQLUserDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Clear - Positive Test")
    void clearSuccess() throws DataAccessException {
        String username = "authorizedUser";
        String password = "password";
        String email = "hello@world.com";
        UserData userData = new UserData(username, password, email);
        userDataAccess.addUser(userData);
        userDataAccess.clearUserData();
        assertTrue(userDataAccess.emptyUserData());
    }

}

