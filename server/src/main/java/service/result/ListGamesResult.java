package service.result;

import model.GameData;

import java.util.List;


public sealed interface ListGamesResult {
    record Success(List<GameData> games) implements ListGamesResult {}
    record Error(String errorMessage) implements ListGamesResult {}
}

