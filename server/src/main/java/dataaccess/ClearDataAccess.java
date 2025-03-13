package dataaccess;


public class ClearDataAccess {
    UserDataAccess userDataAccess = new MemoryUserDataAccess();
    AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
    GameDataAccess gameDataAccess = new MemoryGameDataAccess();
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
