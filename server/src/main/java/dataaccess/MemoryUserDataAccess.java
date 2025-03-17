package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    private static final Map<String, UserData> USER_DATA = new HashMap<>();
    public UserData findUser(String username) {
        if (USER_DATA.get(username) == null) {
            return null;
        } else {
            return USER_DATA.get(username);
        }
    }
    public void addUser(UserData user) {
        USER_DATA.put(user.username(), new UserData(user.username(), user.password(), user.email()));
    }
    public void clearUserData() {
        USER_DATA.clear();
    }
    public Boolean emptyUserData() {
        return USER_DATA.isEmpty();
    }
}
