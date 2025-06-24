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

    // name of piece
        ChessPiece.PieceType piece = board.getPiece(myPosition).getPieceType();

        Collection<ChessMove> validMoves = new ArrayList<>();
        switch(piece){
        case BISHOP:
        //up right
            validMoves.addAll(UpRight(myPosition, row, col));
        //up left
            validMoves.addAll(UpLeft(myPosition, row, col));
        //up right
            validMoves.addAll(BottomRight(myPosition, row, col));
        //bottom left
            validMoves.addAll(BottomLeft(myPosition, row, col));
        }

        return validMoves;
    }

    public Collection<ChessMove> UpRight(ChessPosition startposition, int row, int col){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row += 1;
            col += 1;
            ChessPosition finalPosition = new ChessPosition(row, col);
            ChessMove chessmove = new ChessMove(startposition, finalPosition, null);
            moves.add(chessmove);
        }
        return moves;
    }

    public Collection<ChessMove> UpLeft(ChessPosition startposition, int row, int col){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row -= 1;
            col += 1;
            ChessPosition finalPosition = new ChessPosition(row, col);
            ChessMove chessmove = new ChessMove(startposition, finalPosition, null);
            moves.add(chessmove);
        }
        return moves;
    }
    public Collection<ChessMove> BottomRight(ChessPosition startposition, int row, int col){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row += 1;
            col -= 1;
            ChessPosition finalPosition = new ChessPosition(row, col);
            ChessMove chessmove = new ChessMove(startposition, finalPosition, null);
            moves.add(chessmove);
        }
        return moves;
    }
    public Collection<ChessMove> BottomLeft(ChessPosition startposition, int row, int col){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row < 8 && col < 8) && (row > 1 && col > 1)) {
            row -= 1;
            col -= 1;
            ChessPosition finalPosition = new ChessPosition(row, col);
            ChessMove chessmove = new ChessMove(startposition, finalPosition, null);
            moves.add(chessmove);
        }
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
