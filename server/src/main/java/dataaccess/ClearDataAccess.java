package dataaccess;


public class ClearDataAccess {
    public static void clearDatabase() {
        UserDataAccess.clearUserData();
        AuthDataAccess.clearAuthData();
        GameDataAccess.clearGameData();
    }
}
