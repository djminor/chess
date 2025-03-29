package client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataaccess.ClearDataAccess;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    ClearDataAccess clearDataAccess = new ClearDataAccess();
    Gson SERIALIZER = new Gson();



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

    @Test
    @DisplayName("Logout - Positive Test")
    public void logoutSuccessTest() throws Exception {
        facade.register("validUser", "password", "hello@world.com");
        var authData = facade.login("validUser", "password");
        JsonObject jsonResponse = SERIALIZER.fromJson(authData, JsonObject.class);
        String authToken = jsonResponse.get("authToken").getAsString();
        var logoutResult = facade.logout(authToken);
        assertFalse(logoutResult.contains("Error"));
    }

    @Test
    @DisplayName("Logout - Negative Test")
    public void logoutFailureTest() throws Exception {
        var logoutResult = facade.logout("fake token");
        assertTrue(logoutResult.contains("Error"));
    }

    @Test
    @DisplayName("List - Positive Test")
    public void listSuccessTest() throws Exception {
        facade.register("validUser", "password", "hello@world.com");
        var authData = facade.login("validUser", "password");
        JsonObject jsonResponse = SERIALIZER.fromJson(authData, JsonObject.class);
        String authToken = jsonResponse.get("authToken").getAsString();
        facade.createGame("testGame", authToken);
        String listResult = facade.listGames(authToken);
        assertFalse(listResult.isEmpty());
    }

    @Test
    @DisplayName("List - Negative Test")
    public void listFailureTest() throws Exception {
        String listResult = facade.listGames("Fake token");
        assertTrue(listResult.contains("Error"));
    }

    @Test
    @DisplayName("Create - Positive Test")
    public void createSuccessTest() throws Exception {
        facade.register("validUser", "password", "hello@world.com");
        var authData = facade.login("validUser", "password");
        JsonObject jsonResponse = SERIALIZER.fromJson(authData, JsonObject.class);
        String authToken = jsonResponse.get("authToken").getAsString();
        String createResult = facade.createGame("testGame", authToken);
        assertFalse(createResult.contains("Error"));
    }

    @Test
    @DisplayName("Create - Negative Test")
    public void createFailureTest() throws Exception {
        String createResult = facade.createGame("testGame", "fake token");
        assertTrue(createResult.contains("Error"));
    }

}
