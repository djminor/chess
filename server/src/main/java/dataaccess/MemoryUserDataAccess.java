package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    private static final Map<String, UserData> users = new HashMap<>();
    public UserData findUser(String username) {
        if (users.get(username) == null) {
            return null;
        } else {
            return users.get(username);
        }
    }
    public void addUser(UserData user) {
        users.put(user.username(), new UserData(user.username(), user.password(), user.email()));
    }
    public void clearUserData() {
        users.clear();
    }
    public Boolean emptyUserData() {
        return users.isEmpty();
    }
}
