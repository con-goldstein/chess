package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
private ChessGame.TeamColor pieceColor;
private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
    // get row and col where piece is
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

    // get piece type & color
        ChessPiece myPiece = board.getPiece(myPosition);
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        ChessPiece.PieceType myPieceType = board.getPiece(myPosition).getPieceType();

        Collection<ChessMove> validMoves = new ArrayList<>();
        switch(myPieceType){
            case BISHOP:
            //up right
                validMoves.addAll(UpRight(myPosition, row, col, board, myPieceColor));
            //up left
                validMoves.addAll(UpLeft(myPosition, row, col, board, myPieceColor));
            //up right
                validMoves.addAll(BottomRight(myPosition, row, col, board, myPieceColor));
            //bottom left
                validMoves.addAll(BottomLeft(myPosition, row, col, board, myPieceColor));
        }
        return validMoves;
    }

    public Collection<ChessMove> UpRight(ChessPosition startposition, int row, int col,
                                         ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row += 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        return moves;
    }
    public Collection<ChessMove> UpLeft(ChessPosition startposition, int row, int col,
                                        ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row -= 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        return moves;
    }

    public Collection<ChessMove> BottomRight(ChessPosition startposition, int row, int col,
                                             ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row += 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        return moves;
    }
    public Collection<ChessMove> BottomLeft(ChessPosition startposition, int row, int col,
                                            ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row -= 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
                moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
                if (newPiece != null) {
                    break;
                }
        }
        return moves;
    }

    public Collection<ChessMove> AddToMoves(ChessPiece newPiece, ChessGame.TeamColor TeamColor, int row,
                                            int col, ChessPosition startposition, Collection<ChessMove> moves, ChessPosition newPosition){
        //check if there is a piece on the square or not (null)
        if (newPiece != null){
            ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
            //if piece, check if color is same or opposite
            if (TeamColor != newPieceColor){
                ChessPosition finalPosition = new ChessPosition(row, col);
                moves = FinishMoves(startposition, finalPosition, moves);
            }
        }
        else{
            ChessPosition finalPosition = new ChessPosition(row, col);
            moves = FinishMoves(startposition, finalPosition, moves);
        }
        return moves;
    }

    // Create chessmove and add to moves
    public Collection<ChessMove> FinishMoves(ChessPosition startPosition, ChessPosition finalPosition, Collection<ChessMove> moves){
        ChessMove chessmove = new ChessMove(startPosition, finalPosition, null);
        moves.add(chessmove);
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public String toString(){
        String string = String.format("Color = %s, type = %s", pieceColor, type);
        return string;
    }
}
