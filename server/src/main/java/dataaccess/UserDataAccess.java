package dataaccess;

import model.UserData;

public interface UserDataAccess {
    static UserData findUser(String username) throws DataAccessException {
        return null;
    }

    static void addUser(UserData user) throws DataAccessException {

    }

    static void clearUserData() throws DataAccessException {

    }

    static Boolean emptyUserData() throws DataAccessException {
        return null;
    }
}
