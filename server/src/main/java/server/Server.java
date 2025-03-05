package server;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.*;

import model.UserData;
import handler.Handler;

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
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::ClearDatabase);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) {
        return Handler.register(request, response);
    }

    private Object login(Request request, Response response) {
        return Handler.login(request, response);
    }

    private Object logout(Request request, Response response) {
        return Handler.logout(request, response);
    }

    private Object listGames(Request request, Response response) {
        return Handler.listGames(request, response);
    }

    private Object createGame(Request request, Response response) { return Handler.createGame(request, response); }

    private Object joinGame(Request request, Response response) { return Handler.joinGame(request, response); }

    private Object ClearDatabase(Request request, Response response) {
        return Handler.clear(response);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
