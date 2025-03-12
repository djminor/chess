package dataaccess;

import model.AuthData;

public interface AuthDataAccess {

    AuthData findAuthData(String authToken) throws DataAccessException;

    void deleteAuthData(String authToken) throws DataAccessException;

    void addAuth(String username, String authToken) throws DataAccessException;

    void clearAuthData() throws DataAccessException;

    Boolean emptyAuthData() throws DataAccessException;
}
