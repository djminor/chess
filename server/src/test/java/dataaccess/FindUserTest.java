package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FindUserTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MySQLUserDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Find User - Positive Test")
    void findAuthDataSuccessTest() throws DataAccessException {
        String username = "authorizedUser";
        String password = "password";
        String email = "hello@world.com";
        UserData userData = new UserData(username, password, email);
        userDataAccess.addUser(userData);
        assertEquals(username, userDataAccess.findUser(username).username());
        assertEquals(email, userDataAccess.findUser(username).email());
    }

    @Test
    @DisplayName("Find User - Negative Test")
    void findAuthDataFailureTest() throws DataAccessException {
        String username = "unauthorizedUser";
        assertNull(userDataAccess.findUser(username));
    }
}

