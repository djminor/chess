package dataaccess;


public class ClearDataAccess {
    public static void clearDatabase() {
        UserDataAccess.clearUserData();
        AuthDataAccess.clearAuthData();
        GameDataAccess.clearGameData();
    }
    public static Boolean databaseCleared() {
        return UserDataAccess.emptyUserData() &&
                AuthDataAccess.emptyAuthData() &&
                GameDataAccess.emptyGameData();
    }
}
