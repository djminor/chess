package model;

import chess.ChessGame;  // Assuming you have a ChessGame class

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {}
