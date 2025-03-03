package server;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.*;

import model.UserData;

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
        Spark.delete("/session", this::logout);
        Spark.delete("/db", this::ClearDatabase);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) {
        JsonObject reqJson = gson.fromJson(request.body(), JsonObject.class);
        if (!reqJson.has("username") || reqJson.get("username").isJsonNull() || reqJson.get("username").getAsString().isEmpty() ||
                !reqJson.has("password") || reqJson.get("password").isJsonNull() || reqJson.get("password").getAsString().isEmpty() ||
                !reqJson.has("email") || reqJson.get("email").isJsonNull() || reqJson.get("email").getAsString().isEmpty()) {

            JsonObject json = new JsonObject();
            json.addProperty("message", "Error: Bad Request");
            response.status(400);
            return gson.toJson(json);
        }
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();
        String email = reqJson.get("email").getAsString();
        String authToken = "";

        UserData userData = service.getUser(username);
        if (userData == null) {
            service.createUser(username, password, email);
            authToken = service.createAuth(username);
            JsonObject json = new JsonObject();
            json.addProperty("username", username);
            json.addProperty("authToken", authToken);
            response.status(200);
            return gson.toJson(json);
        } else {
            JsonObject json = new JsonObject();
            json.addProperty("message", "Error: Already taken");
            response.status(403);
            return gson.toJson(json);
        }
    }

    private Object login(Request request, Response response) {
        JsonObject reqJson = gson.fromJson(request.body(), JsonObject.class);
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();

        if (service.getUser(username) == null) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return gson.toJson(errorJson);
        }

        if (verifyPassword(username, password)) {
            String authToken = service.createAuth(username);
            JsonObject json = new JsonObject();
            json.addProperty("username", username);
            json.addProperty("authToken", authToken);
            response.status(200);
            return gson.toJson(json);
        } else {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return gson.toJson(errorJson);
        }
    }

    private Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");
        System.out.print(request.queryParams());
            if (service.getAuth(authToken) != null) {
                service.deleteAuth(authToken);
                response.status(200);
                return gson.toJson(new JsonObject());
            } else {
                JsonObject errorJson = new JsonObject();
                errorJson.addProperty("message", "Error: Unauthorized");
                response.status(401);
                return gson.toJson(errorJson);
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
