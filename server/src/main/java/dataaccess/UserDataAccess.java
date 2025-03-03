package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDataAccess {
    private static final Map<String, UserData> users = new HashMap<>();
    public static UserData findUser(String username) {
        if (users.get(username) == null) {
            return null;
        } else {
            return users.get(username);
        }
    }
    public static void addUser(String username, String password, String email) {
        users.put(username, new UserData(username, password, email));
    }
    public static void clearUserData() {
        users.clear();
    }
}
