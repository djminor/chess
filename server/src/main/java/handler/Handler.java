package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import service.UserService;
import service.request.*;
import service.result.*;
import spark.Request;
import spark.Response;

public class Handler {
    private static final Gson serializer = new Gson();

    public static Object register(Request request, Response response) {
        JsonObject reqJson = serializer.fromJson(request.body(), JsonObject.class);
        if (!reqJson.has("username") || reqJson.get("username").isJsonNull() || reqJson.get("username").getAsString().isEmpty() ||
                !reqJson.has("password") || reqJson.get("password").isJsonNull() || reqJson.get("password").getAsString().isEmpty() ||
                !reqJson.has("email") || reqJson.get("email").isJsonNull() || reqJson.get("email").getAsString().isEmpty()) {

            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Bad Request");
            response.status(400);
            return serializer.toJson(errorJson);
        }
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();
        String email = reqJson.get("email").getAsString();

        RegisterResult result = UserService.register(new RegisterRequest(username, password, email));
        if (!result.authToken().isEmpty()) {
            response.status(200);
        } else {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Bad Request");
            response.status(403);
            return serializer.toJson(errorJson);
        }
        return serializer.toJson(result);
    }

    public static Object login(Request request, Response response) {
        JsonObject reqJson = serializer.fromJson(request.body(), JsonObject.class);
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();

        LoginResult result = UserService.login(new LoginRequest(username, password));

        if (result.authToken().isEmpty()) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return serializer.toJson(errorJson);
        }
        response.status(200);
        return serializer.toJson(result);
    }

    public static Object logout(Request request, Response response) {
        String authToken = request.headers("Authorization");
        System.out.print(authToken);
        LogoutResult result = UserService.logout(new LogoutRequest(authToken));
        if (result.authToken().contains("Error")) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return serializer.toJson(errorJson);
        }
        response.status(200);
        return serializer.toJson(new JsonObject());
    }

    public static Object listGames(Request request, Response response) {
        String authToken = request.headers("Authorization");
        ListGamesResult result = UserService.listGames(new ListGamesRequest(authToken));
        System.out.print(serializer.toJson(result));
        if (result instanceof ListGamesResult.Error errorResult) {
            response.status(401);
            return serializer.toJson(errorResult);
        }
        response.status(200);
        return serializer.toJson(result);
    }

    public static Object createGame(Request request, Response response) {
        JsonObject reqJson = serializer.fromJson(request.body(), JsonObject.class);
        String authToken = request.headers("Authorization");
        String gameName = reqJson.get("gameName").getAsString();
        CreateGameResult result = UserService.createGame(new CreateGameRequest(gameName, authToken));
        if (result instanceof CreateGameResult.Error errorResult) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return serializer.toJson(errorJson);
        }
        response.status(200);
        return serializer.toJson(result);
    }

}
