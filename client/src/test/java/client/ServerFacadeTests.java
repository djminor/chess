package client;

import dataaccess.ClearDataAccess;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.DisabledIfSystemProperties;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    ClearDataAccess clearDataAccess = new ClearDataAccess();


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        clearDataAccess.clearDatabase();
    }

    @Test
    @DisplayName("Register - Positive Test")
    public void registerSuccessTest() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.length() > 10);
    }

    @Test
    @DisplayName("Register - Negative Test")
    public void registerFailureTest() throws Exception {
        var authData = facade.register("player1", "password", "");
        assertTrue(authData.contains("Error"));
    }

    @Test
    @DisplayName("Login - Positive Test")
    public void loginSuccessTest() throws Exception {
        facade.register("validUser", "password", "hello@world.com");
        var authData = facade.login("validUser", "password");
        assertTrue(authData.length() > 10);
    }

    @Test
    @DisplayName("Login - Negative Test")
    public void loginFailureTest() throws Exception {
        var authData = facade.login("validUser", "password");
        assertTrue(authData.contains("Error"));
    }

}
