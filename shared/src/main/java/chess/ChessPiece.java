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
                break;
        };
        return moveList;
    }

    private void calculatePawnMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {
        int moveDirection = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int row = position.getRow();
        int col = position.getColumn();

        // Single forward move
        ChessPosition forward = new ChessPosition(row + moveDirection, col);
        if (!board.isPositionOutOfBounds(forward) && board.getPiece(forward) == null) {
            // Add promotion if the pawn reaches the last row
            if ((piece.getTeamColor() == ChessGame.TeamColor.BLACK && forward.getRow() == 1) ||
                    (piece.getTeamColor() == ChessGame.TeamColor.WHITE && forward.getRow() == 8)) {
                moves.add(new ChessMove(position, forward, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(position, forward, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(position, forward, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(position, forward, ChessPiece.PieceType.KNIGHT));
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
            if (!board.isPositionOutOfBounds(doubleForward) && board.getPiece(doubleForward) == null &&
                    !board.isPositionOutOfBounds(intermediate) && board.getPiece(intermediate) == null) {
                moves.add(new ChessMove(position, doubleForward, null));
            }
        }

        // Pawn captures
        int[][] captureOffsets = {{moveDirection, -1}, {moveDirection, 1}};
        for (int[] offset : captureOffsets) {
            ChessPosition capturePosition = new ChessPosition(row + offset[0], col + offset[1]);
            if (!board.isPositionOutOfBounds(capturePosition)) {
                ChessPiece targetPiece = board.getPiece(capturePosition);
                if (targetPiece != null && board.isOpponentPiece(capturePosition, piece)) {
                    if (((piece.getTeamColor() == ChessGame.TeamColor.WHITE && capturePosition.getRow() == 8)) || (piece.getTeamColor() == ChessGame.TeamColor.BLACK && capturePosition.getRow() == 1)) {
                        moves.add(new ChessMove(position, capturePosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, capturePosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, capturePosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, capturePosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        moves.add(new ChessMove(position, capturePosition, null));
                    }
                }
            }
        }
    }

    private void calculateSlidingPieceMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves, int[][] directions) {
        for (int[] direction : directions) {
            int col = position.getColumn();
            int row = position.getRow();

            while (true) {
                col += direction[0];
                row += direction[1];
                ChessPosition newPosition = new ChessPosition(row, col);

                if (board.isPositionOutOfBounds(newPosition)) break;

                ChessPiece targetPiece = board.getPiece(newPosition);

                if (targetPiece == null) {
                    moves.add(new ChessMove(position, newPosition, null));
                } else {
                    if (targetPiece.getTeamColor() != piece.getTeamColor()) {
                        moves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
            }
        }
    }
    private void calculateKnightMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {
        int[][] knightMoves = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };
        addKingKnightMoves(board, position, piece, moves, knightMoves);
    }

    private void calculateKingMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves) {
        int[][] kingMoves = {
                {-1, -1}, {-1, 0}, {-1, 1},
                { 0, -1},          { 0, 1},
                { 1, -1}, { 1, 0}, { 1, 1}
        };
        addKingKnightMoves(board, position, piece, moves, kingMoves);
    }

    private void addKingKnightMoves(ChessBoard board, ChessPosition position, ChessPiece piece, Collection<ChessMove> moves, int[][] moveOffsets) {
        for (int[] move : moveOffsets) {
            int newRow = position.getRow() + move[0];
            int newCol = position.getColumn() + move[1];
            ChessPosition newPosition = new ChessPosition(newRow, newCol);

            if (!board.isPositionOutOfBounds(newPosition)) {
                ChessPiece targetPiece = board.getPiece(newPosition);
                if (targetPiece == null || board.isOpponentPiece(newPosition, piece)) {
                    moves.add(new ChessMove(position, newPosition, null));
                }
            }
        }
    }

}
