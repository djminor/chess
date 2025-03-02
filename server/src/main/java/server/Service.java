package server;

import java.util.Objects;

import java.util.UUID;

public class Service {

    DataAccess dataAccess = new DataAccess();

    public DataAccess.UserData getUser(String username) {
        return dataAccess.findUser(username);
    }

    public void createUser(String username, String password, String email) {
        dataAccess.addUser(username, password, email);
    }

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        dataAccess.addAuth(username, authToken);

        return authToken;
    }

    public void clear() {
        dataAccess.clearDatabase();
    }
}
