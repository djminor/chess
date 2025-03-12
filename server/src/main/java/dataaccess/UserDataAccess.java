package dataaccess;

import model.UserData;

public interface UserDataAccess {
    UserData findUser(String username) throws DataAccessException;

    void addUser(UserData user) throws DataAccessException;

    void clearUserData() throws DataAccessException;

    Boolean emptyUserData() throws DataAccessException;
}
