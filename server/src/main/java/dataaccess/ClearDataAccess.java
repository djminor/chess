package dataaccess;


public class ClearDataAccess {
    UserDataAccess userDataAccess = new MySQLUserDataAccess();
    AuthDataAccess authDataAccess = new MySQLAuthDataAccess();
    GameDataAccess gameDataAccess = new MySQLGameDataAccess();
    public void clearDatabase() throws DataAccessException {
        userDataAccess.clearUserData();
        authDataAccess.clearAuthData();
        gameDataAccess.clearGameData();
    }
    public Boolean databaseCleared() throws DataAccessException {
        return Boolean.TRUE.equals(userDataAccess.emptyUserData()) &&
                authDataAccess.emptyAuthData() &&
                gameDataAccess.emptyGameData();
    }
}
