package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.UserService;
import service.request.JoinGameRequest;
import service.result.JoinGameResult;


@WebSocket
public class WSServer {
    private final Gson serializer = new Gson();
    private final UserService userService = new UserService();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        JsonObject request = serializer.fromJson(message, JsonObject.class);
        String commandType = request.get("commandType").getAsString();
        String authToken = request.get("authToken").getAsString();
        int gameID = request.get("gameID").getAsInt();

        JsonObject response = new JsonObject();

        if (commandType.equals("CONNECT")) {
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", gameID);
            JoinGameResult joinGameResult = userService.joinGame(joinGameRequest);
            response.addProperty("serverMessageType", "LOAD_GAME");
            response.addProperty("game", String.valueOf(joinGameResult));
        } else {
            response.addProperty("serverMessageType", "ERROR");
            response.addProperty("errorMessage", "Unknown command type: " + commandType);
        }

        session.getRemote().sendString(serializer.toJson(response));
    }
}