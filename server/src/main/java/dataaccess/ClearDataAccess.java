package dataaccess;


public class ClearDataAccess {
    public static void clearDatabase() throws DataAccessException {
        UserDataAccess.clearUserData();
        AuthDataAccess.clearAuthData();
        GameDataAccess.clearGameData();
    }
    public static Boolean databaseCleared() throws DataAccessException {
        return Boolean.TRUE.equals(UserDataAccess.emptyUserData()) &&
                AuthDataAccess.emptyAuthData() &&
                GameDataAccess.emptyGameData();
    }
}
