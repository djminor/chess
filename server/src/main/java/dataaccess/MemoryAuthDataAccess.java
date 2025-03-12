package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDataAccess implements AuthDataAccess {
    private static final Map<String, AuthData> authorizedUsers = new HashMap<>();

    public AuthData findAuthData(String authToken) {
        return authorizedUsers.get(authToken);
    }
    public void deleteAuthData(String authToken) {
        if (authorizedUsers.get(authToken) == null) {
            System.out.print("Not sure how this even happened");
        } else {
            authorizedUsers.remove(authToken);
        }
    }
    public void addAuth(String username, String authToken) {
        authorizedUsers.put(authToken, new AuthData(username, authToken));
    }
    public void clearAuthData() {
        authorizedUsers.clear();
    }
    public Boolean emptyAuthData() {
        return authorizedUsers.isEmpty();
    }
}