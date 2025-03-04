package service.result;

public sealed interface CreateGameResult {
    record Success(int gameID) implements CreateGameResult {}
    record Error(String error) implements CreateGameResult {}
}
