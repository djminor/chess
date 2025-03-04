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
