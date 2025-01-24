package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, pieceType);
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

        // Single forward move
        ChessPosition forward = new ChessPosition(row + moveDirection, col);
        if (board.isPositionOutOfBounds(forward) && board.getPiece(forward) == null) {
            // Add promotion if the pawn reaches the last row
            if ((piece.getTeamColor() == ChessGame.TeamColor.BLACK && forward.getRow() == 1) ||
                    (piece.getTeamColor() == ChessGame.TeamColor.WHITE && forward.getRow() == 8)) {
                addPromotionMoves(position, forward, moves);
            } else {
                moves.add(new ChessMove(position, forward, null));
            }
        }

        // Double forward move (only if the pawn is in its initial position)
        if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && row == 2) ||
                (piece.getTeamColor() == ChessGame.TeamColor.BLACK && row == 7)) {
            ChessPosition intermediate = new ChessPosition(row + moveDirection, col);  // Intermediate square
            ChessPosition doubleForward = new ChessPosition(row + 2 * moveDirection, col);

            // Ensure both the square two ahead and the intermediate square are clear
            if (board.isPositionOutOfBounds(doubleForward) && board.getPiece(doubleForward) == null &&
                    board.isPositionOutOfBounds(intermediate) && board.getPiece(intermediate) == null) {
                moves.add(new ChessMove(position, doubleForward, null));
            }
        }

        // Diagonal capture positions (both forward-left and forward-right)
        if (moveDirection == -1) {
            int [][] captureOffsets = {{1, -1}, {1, 1}};
            for (int[] offset : captureOffsets) {
                int rowOffset = offset[0];
                int colOffset = offset[1];
                ChessPosition capturePosition = new ChessPosition(row + rowOffset, col + colOffset);

                // Make sure the position is within bounds
                if (!board.isPositionOutOfBounds(capturePosition)) {
                    ChessPiece targetPiece = board.getPiece(capturePosition);

                    // Check if there is an opponent's piece to capture
                    if (targetPiece != null && board.isOpponentPiece(capturePosition, piece)) {
                        moves.add(new ChessMove(position, capturePosition, null));  // Regular capture
                    }
                }
            }
        } else {
            int[][] captureOffsets = {{-1, -1}, {-1, 1}};  // Forward-left and forward-right diagonals
            for (int[] offset : captureOffsets) {
                int rowOffset = offset[0];
                int colOffset = offset[1];
                ChessPosition capturePosition = new ChessPosition(row + rowOffset, col + colOffset);

                // Make sure the position is within bounds
                if (!board.isPositionOutOfBounds(capturePosition)) {
                    ChessPiece targetPiece = board.getPiece(capturePosition);

                    // Check if there is an opponent's piece to capture
                    if (targetPiece != null && board.isOpponentPiece(capturePosition, piece)) {
                        moves.add(new ChessMove(position, capturePosition, null));  // Regular capture
                    }
                }
            }
        }
    }

    private void addPromotionMoves(ChessPosition from, ChessPosition to, Collection<ChessMove> moves) {
        moves.add(new ChessMove(from, to, ChessPiece.PieceType.QUEEN));
    }
    private void calculateSlidingPieceMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves, int[][] directions) {

    }
    private void calculateKnightMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {

    }
    private void calculateKingMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {

    }
}
