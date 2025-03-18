package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class EmptyUserDataTest {
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    UserDataAccess userDataAccess = new MySQLUserDataAccess();

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Empty User Data - Positive Test")
    void emptyGameDataTableTest() throws DataAccessException {
        boolean isEmpty = userDataAccess.emptyUserData();
        assertTrue(isEmpty);
    }

    @Test
    @DisplayName("Empty User Data - Negative Test")
    void nonEmptyGameDataTableTest() throws DataAccessException {
        String username = "authorizedUser";
        String password = "password";
        String email = "hello@world.com";
        UserData userData = new UserData(username, password, email);
        userDataAccess.addUser(userData);
        boolean isEmpty = userDataAccess.emptyUserData();
        assertFalse(isEmpty);
    }
}

