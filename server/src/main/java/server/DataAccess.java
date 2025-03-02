package server;
import java.util.HashMap;
import java.util.Map;

public class DataAccess {

    public Map<String, UserData> users = new HashMap<>();
    public Map<String, AuthData> authorizedUsers = new HashMap<>();

    public UserData findUser(String username) {
        if (users.get(username) == null) {
            return null;
        } else {
            return users.get(username);
        }
    }

    public void addUser(String username, String password, String email) {
        users.put(username, new UserData(username, password, email));
    }

    public void addAuth(String username, String authToken) {
        authorizedUsers.put(username, new AuthData(username, authToken));
    }

    public void clearDatabase() {
        clearUserData();
        clearAuthData();
        clearGameData();
    }

    public void clearUserData() {

    }
    public void clearAuthData() {

    }
    public void clearGameData() {

    }

    public record UserData(String username, String password, String email) {
    }

    public record AuthData(String username, String authToken) {

    }
}
