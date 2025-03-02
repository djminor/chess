package server;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.*;

import java.util.Objects;


public class Server {

    Service service = new Service();

    private static final Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (this::register));
        Spark.post("/session", (this::login));
        Spark.delete("/db", this::ClearDatabase);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) {
        JsonObject reqJson = gson.fromJson(request.body(), JsonObject.class);
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();
        String email = reqJson.get("email").getAsString();
        String authToken = "";

        DataAccess.UserData userData = service.getUser(username);
        if (userData == null) {
            service.createUser(username, password, email);
            authToken = service.createAuth(username);
        }

        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("authToken", authToken);
        response.status(200);
        return gson.toJson(json);
    }

    private Object login(Request request, Response response) {
        JsonObject reqJson = gson.fromJson(request.body(), JsonObject.class);
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();

        if (username == null || username.isEmpty()) {
            response.status(400);
            return "{\"error\": \"Username is required\"}";
        }

        if (verifyPassword(username, password)) {
            String authToken = service.createAuth(username);
            JsonObject json = new JsonObject();
            json.addProperty("username", username);
            json.addProperty("authToken", authToken);
            response.status(200);
            return gson.toJson(json);
        } else {
            response.status(401);
            return "{\"error\": \"Password is incorrect\"}";
        }
    }

    private boolean verifyPassword(String username, String password) {
        String expectedPassword = service.getUser(username).password();
        return Objects.equals(expectedPassword, password);
    }


    private Object ClearDatabase(Request request, Response response) {
        service.clear();
        response.status(200);
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
