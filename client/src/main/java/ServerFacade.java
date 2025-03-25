import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class ServerFacade {
    private static final String SERVER_URL = "http://localhost:8080"; // Change if needed
    private final Gson SERIALIZER = new Gson();

    public String register(String username, String password, String email) throws Exception {
        JsonObject reqJson = new JsonObject();
        reqJson.addProperty("username", username);
        reqJson.addProperty("password", password);
        reqJson.addProperty("email", email);
        return sendPostRequest("/user", reqJson.toString(), null);
    }

    private String sendPostRequest(String endpoint, String jsonBody, String authToken) throws Exception {
        URL url = new URL(SERVER_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }
        connection.setDoOutput(true);
        try (OutputStream os = connection.getOutputStream()) {
            os.write(jsonBody.getBytes());
            os.flush();
        }
        return getResponse(connection);
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();
        InputStream inputStream = (status < 400) ?
                connection.getInputStream() :
                connection.getErrorStream();
        try (Scanner scanner = new Scanner(inputStream)) {
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine()).append("\n");
            }
            return (status == 200) ? response.toString() : "Error: " + response.toString();
        }

    }
}
