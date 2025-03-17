package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDataAccess implements AuthDataAccess {
    private static final Map<String, AuthData> AUTH_DATA = new HashMap<>();

    public AuthData findAuthData(String authToken) {
        return AUTH_DATA.get(authToken);
    }
    public void deleteAuthData(String authToken) {
        if (AUTH_DATA.get(authToken) == null) {
            System.out.print("Not sure how this even happened");
        } else {
            AUTH_DATA.remove(authToken);
        }
    }
    public void addAuth(AuthData authData) {
        AUTH_DATA.put(authData.authToken(), new AuthData(authData.username(), authData.authToken()));
    }
    public void clearAuthData() {
        AUTH_DATA.clear();
    }
    public Boolean emptyAuthData() {
        return AUTH_DATA.isEmpty();
    }
}