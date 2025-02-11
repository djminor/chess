package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    public TeamColor teamTurn;
    public ChessBoard currentBoard = new ChessBoard();
    public ChessBoard tempBoard = new ChessBoard();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, currentBoard);
    }

    public ChessGame() {
        currentBoard.resetBoard();
        tempBoard = currentBoard;
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.currentBoard.getPiece(startPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        if(piece == null) {
            return Collections.emptyList();
        } else {
            for(ChessMove move : piece.pieceMoves(currentBoard, startPosition)) {
                ChessPiece enemyPiece = tempBoard.getPiece(move.getEndPosition());
                tempBoard.addPiece(move.getEndPosition(), currentBoard.getPiece(move.getStartPosition()));
                tempBoard.removePiece(move.getStartPosition());
                ChessGame tempGame = new ChessGame();
                tempGame.setBoard(tempBoard);
                if (!tempGame.isInCheck(piece.getTeamColor())) {
                    moves.add(move);
                }
                tempBoard.addPiece(move.getStartPosition(), tempBoard.getPiece(move.getEndPosition()));
                tempBoard.removePiece(move.getEndPosition());
                if(enemyPiece != null) {
                    tempBoard.addPiece(move.getEndPosition(), enemyPiece);
                }
            }
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = currentBoard.getPiece(move.getStartPosition());

        if(piece == null) {
            throw new InvalidMoveException("No piece at selected position");
        }

        if(piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not selected piece's turn");
        }

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if(!validMoves.contains(move)) {
            throw new InvalidMoveException("King cannot be left in check");
        }
        if(move.getPromotionPiece() != null) {
            currentBoard.addPiece(move.getEndPosition(), new ChessPiece(teamTurn, move.getPromotionPiece()));
            currentBoard.removePiece(move.getStartPosition());
        }
        if (move.getPromotionPiece() == null) {
            currentBoard.addPiece(move.getEndPosition(), piece);
            currentBoard.removePiece(move.getStartPosition());
        }
        if(teamTurn == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);
        ChessPiece king = currentBoard.getPiece(kingPosition);
        int row = kingPosition.getRow();
        int col = kingPosition.getColumn();

        // King in check on a diagonal
        int[][] diagonalDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        for (int[] direction : diagonalDirections) {
            int currentRow = row;
            int currentCol = col;

            while (true) {
                currentCol += direction[0];
                currentRow += direction[1];
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);

                if (currentBoard.isPositionOutOfBounds(newPosition)) break;

                ChessPiece enemyPiece = currentBoard.getPiece(newPosition);

                if (enemyPiece != null) {
                    if (enemyPiece.getTeamColor() != king.getTeamColor()) {
                        if ((enemyPiece.getPieceType() == ChessPiece.PieceType.PAWN || enemyPiece.getPieceType() == ChessPiece.PieceType.KING) &&
                                (newPosition.getColumn() == kingPosition.getColumn() + 1
                                        || newPosition.getColumn() == kingPosition.getColumn() - 1)) {
                            return true;
                        }
                        if (enemyPiece.getPieceType() == ChessPiece.PieceType.BISHOP
                                || enemyPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            return true;
                        }
                    }
                    break;
                }
            }
        }

        // King in check on a straightaway
        int[][] straightDirections = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        for (int[] direction : straightDirections) {
            int currentRow = row;
            int currentCol = col;

            while(true) {
                currentCol += direction[0];
                currentRow += direction[1];
                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);

                if (currentBoard.isPositionOutOfBounds(newPosition)) break;

                ChessPiece enemyPiece = currentBoard.getPiece(newPosition);

                if (enemyPiece != null) {
                    if (enemyPiece.getTeamColor() != king.getTeamColor()) {
                        if(enemyPiece.getPieceType() == ChessPiece.PieceType.KING && (newPosition.getRow() == kingPosition.getRow() + 1
                                || newPosition.getRow() == kingPosition.getRow() - 1)
                                || newPosition.getColumn() == kingPosition.getColumn() - 1
                                || newPosition.getColumn() == kingPosition.getColumn() + 1) {
                            return true;
                        }
                        if (enemyPiece.getPieceType() == ChessPiece.PieceType.ROOK
                                || enemyPiece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                            return true;
                        }
                    }
                    break;
                }
            }
        }

        // King in check by knight
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] move : knightMoves) {
            int currentCol = col + move[0];
            int currentRow = row + move[1];
            ChessPosition checkPosition = new ChessPosition(currentRow, currentCol);

            if (!currentBoard.isPositionOutOfBounds(checkPosition)) {
                ChessPiece checkPiece = currentBoard.getPiece(checkPosition);
                if (checkPiece != null) {
                    if (currentBoard.isOpponentPiece(checkPosition, king)
                            && checkPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            for(int i = 1; i <= 8; i++) {
                for(int j = 1; j <= 8; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if(currentBoard.getPiece(position) != null && currentBoard.getPiece(position).getTeamColor() == teamColor) {
                        if (!validMoves(position).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)) {
            for(int i = 1; i <= 8; i++) {
                for(int j = 1; j <= 8; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    if(currentBoard.getPiece(position) != null && currentBoard.getPiece(position).getTeamColor() == teamColor) {
                        if (!validMoves(position).isEmpty()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board;
        this.tempBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    this.tempBoard.addPiece(position, new ChessPiece(piece.getTeamColor(), piece.getPieceType()));
                }
            }
        }


    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.currentBoard;
    }

    public ChessPosition findKing(TeamColor color) {
        for(int i = 1; i <= 8; i++) {
            for(int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = currentBoard.getPiece(position);
                if(piece != null) {
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == color) {
                        return position;
                    }
                }
            }
        }
        return null;
    }
}
