package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public interface AuthDataAccess {

    static AuthData findAuthData(String authToken) {
        return null;
    }
    static void deleteAuthData(String authToken) {
    }
    static void addAuth(String username, String authToken) {
    }
    static void clearAuthData() {
    }
    static Boolean emptyAuthData() {
        return null;
    }
}
