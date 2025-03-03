package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDataAccess {
    private static final Map<String, AuthData> authorizedUsers = new HashMap<>();

    public static AuthData findAuthData(String authToken) {
        if (authorizedUsers.get(authToken) == null) {
            return null;
        } else {
            return authorizedUsers.get(authToken);
        }
    }
    public static void deleteAuthData(String authToken) {
        if (authorizedUsers.get(authToken) == null) {
            System.out.print("Not sure how this even happened");
        } else {
            authorizedUsers.remove(authToken);
        }
    }
    public static void addAuth(String username, String authToken) {
        authorizedUsers.put(authToken, new AuthData(username, authToken));
    }
    public static void clearAuthData() {
        authorizedUsers.clear();
    }
}
