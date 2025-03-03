package dataaccess;

import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDataAccess {
    private static final Map<Integer, GameData> games = new HashMap<>();
    public static void clearGameData() {
        games.clear();
    }
}
