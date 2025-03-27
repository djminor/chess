package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import service.UserService;
import service.request.*;
import service.result.*;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class Handler {
    private static final Gson SERIALIZER = new Gson();
    UserService userService = new UserService();

    public Object register(Request request, Response response) throws DataAccessException {
        JsonObject reqJson = SERIALIZER.fromJson(request.body(), JsonObject.class);
        if (!reqJson.has("username") || reqJson.get("username").isJsonNull() || reqJson.get("username").getAsString().isEmpty() ||
                !reqJson.has("password") || reqJson.get("password").isJsonNull() || reqJson.get("password").getAsString().isEmpty() ||
                !reqJson.has("email") || reqJson.get("email").isJsonNull() || reqJson.get("email").getAsString().isEmpty()) {

            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Bad Request");
            response.status(400);
            return SERIALIZER.toJson(errorJson);
        }
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();
        String email = reqJson.get("email").getAsString();

        RegisterResult result = userService.register(new RegisterRequest(username, password, email));
        if (!result.authToken().isEmpty()) {
            response.status(200);
        } else {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Bad Request");
            response.status(403);
            return SERIALIZER.toJson(errorJson);
        }
        return SERIALIZER.toJson(result);
    }

    public Object login(Request request, Response response) throws DataAccessException {
        JsonObject reqJson = SERIALIZER.fromJson(request.body(), JsonObject.class);
        String username = reqJson.get("username").getAsString();
        String password = reqJson.get("password").getAsString();

        LoginResult result = userService.login(new LoginRequest(username, password));

        if (result.authToken().isEmpty()) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return SERIALIZER.toJson(errorJson);
        }
        response.status(200);
        return SERIALIZER.toJson(result);
    }

    public Object logout(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("Authorization");
        LogoutResult result = userService.logout(new LogoutRequest(authToken));
        if (result.authToken().contains("Error")) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return SERIALIZER.toJson(errorJson);
        }
        response.status(200);
        return SERIALIZER.toJson(new JsonObject());
    }

    public Object listGames(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("Authorization");
        ListGamesResult result = userService.listGames(new ListGamesRequest(authToken));
        if (result instanceof ListGamesResult.Error) {
            response.status(401);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            return SERIALIZER.toJson(errorJson);
        }
        response.status(200);
        return SERIALIZER.toJson(result);
    }

    public Object createGame(Request request, Response response) throws DataAccessException {
        JsonObject reqJson = SERIALIZER.fromJson(request.body(), JsonObject.class);
        String authToken = request.headers("Authorization");
        String gameName = reqJson.get("gameName").getAsString();
        CreateGameResult result = userService.createGame(new CreateGameRequest(gameName, authToken));
        if (result instanceof CreateGameResult.Error errorResult) {
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            response.status(401);
            return SERIALIZER.toJson(errorJson);
        }
        response.status(200);
        return SERIALIZER.toJson(result);
    }

    public Object joinGame(Request request, Response response) throws DataAccessException {
        JsonObject reqJson = SERIALIZER.fromJson(request.body(), JsonObject.class);
        if (!reqJson.has("playerColor") || reqJson.get("playerColor").isJsonNull()) {
            response.status(400);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: bad request");
            return SERIALIZER.toJson(errorJson);
        }
        if (!reqJson.has("gameID") || reqJson.get("gameID").isJsonNull()) {
            response.status(400);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: bad request");
            return SERIALIZER.toJson(errorJson);
        }
        String authToken = request.headers("Authorization");
        int gameID = reqJson.get("gameID").getAsInt();
        String playerColor = reqJson.get("playerColor").getAsString();
        JoinGameResult result = userService.joinGame(new JoinGameRequest(authToken, playerColor, gameID));
        if (Objects.equals(result.errorMessage(), "Invalid color")) {
            response.status(400);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: bad request");
            return SERIALIZER.toJson(errorJson);
        }
        if (Objects.equals(result.errorMessage(), "Error")) {
            response.status(401);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Unauthorized");
            return SERIALIZER.toJson(errorJson);
        }
        if (Objects.equals(result.errorMessage(), "Steal")) {
            response.status(403);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("message", "Error: Team already taken");
            return SERIALIZER.toJson(errorJson);
        }
        response.status(200);
        return SERIALIZER.toJson(result);
    }
    public Object clear(Response response) throws DataAccessException {
        ClearDBResult result = userService.clearDB();
        if (result.success()) {
            response.status(200);
            JsonObject json = new JsonObject();
            return SERIALIZER.toJson(json);
        }
        response.status(500);
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("message", "Error: Unauthorized");
        return SERIALIZER.toJson(errorJson);
    }
}
