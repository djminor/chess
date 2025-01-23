package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamColor;
    private final PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null) {
            return Collections.emptyList();
        }

        Collection<ChessMove> moveList = new ArrayList<>();

        switch (piece.getPieceType()) {
            case PAWN:
                calculatePawnMoves(board, myPosition, piece, moveList);
                break;
            case ROOK:
                calculateSlidingPieceMoves(board, myPosition, piece, moveList, new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}});
                break;
            case BISHOP:
                calculateSlidingPieceMoves(board, myPosition, piece, moveList, new int[][] {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
                break;
            case QUEEN:
                calculateSlidingPieceMoves(board, myPosition, piece, moveList, new int[][] {
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}
                });
                break;
            case KNIGHT:
                calculateKnightMoves(board, myPosition, piece, moveList);
                break;
            case KING:
                calculateKingMoves(board, myPosition, piece, moveList);
        };
        return moveList;
    }

    private void calculatePawnMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {
        int moveDirection = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition forward = new ChessPosition(col, row + moveDirection);
        moves.add(new ChessMove(position, forward, getPieceType()));
    }
    private void calculateSlidingPieceMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves, int[][] directions) {

    }
    private void calculateKnightMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {

    }
    private void calculateKingMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {

    }
}
