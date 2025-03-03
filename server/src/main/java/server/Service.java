package server;

import model.UserData;
import model.AuthData;

import java.util.UUID;

public class Service {

    public UserData getUser(String username) {
        return dataaccess.UserDataAccess.findUser(username);
    }

    public AuthData getAuth(String authToken) {
        return dataaccess.AuthDataAccess.findAuthData(authToken);
    }

    public void deleteAuth(String authToken) {
        dataaccess.AuthDataAccess.deleteAuthData(authToken);
    }

    public void createUser(String username, String password, String email) {
        dataaccess.UserDataAccess.addUser(username, password, email);
    }

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        dataaccess.AuthDataAccess.addAuth(username, authToken);
        return authToken;
    }

    public void clear() {
        dataaccess.ClearDataAccess.clearDatabase();
    }
}
