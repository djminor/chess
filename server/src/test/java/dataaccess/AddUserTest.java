package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AddUserTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MySQLUserDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Add User - Positive Test")
    void addAuthSuccessTest() throws DataAccessException {
        String username = "authorizedUser";
        String password = "password";
        String email = "hello@world.com";
        UserData userData = new UserData(username, password, email);
        userDataAccess.addUser(userData);
        UserData addedUser = userDataAccess.findUser(username);
        assertNotNull(addedUser);
    }

    @Test
    @DisplayName("Add User - Negative Test")
    void addAuthFailureTest() {
        String username = "unauthorizedUser";
        UserData userData = new UserData(username, null, null);
        assertThrows(RuntimeException.class, () -> userDataAccess.addUser(userData));
    }
}

