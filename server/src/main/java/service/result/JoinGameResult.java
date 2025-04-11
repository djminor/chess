package service.result;

public sealed interface JoinGameResult {
    record Success(String game) implements JoinGameResult {}
    record Failure(String errorMessage) implements JoinGameResult {}
}

