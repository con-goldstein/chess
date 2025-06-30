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
private ChessGame.TeamColor TeamColor;
private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor TeamColor, ChessPiece.PieceType type) {
        this.TeamColor = TeamColor;
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
        return TeamColor;
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
        //get pieceMoves for each type of piece
        switch(myPieceType){
            case BISHOP:
                BishopMovesCalculator Bishop = new BishopMovesCalculator(myPieceColor, myPieceType);
                validMoves = Bishop.pieceMoves(board, myPosition);
                break;
            case KING:
                KingMovesCalculator King = new KingMovesCalculator(myPieceColor, myPieceType);
                validMoves = King.pieceMoves(board, myPosition);
                break;
            case KNIGHT:
                KnightMovesCalculator Knight = new KnightMovesCalculator(myPieceColor, myPieceType);
                validMoves = Knight.pieceMoves(board, myPosition);
            case PAWN:
                PawnMovesCalculator Pawn = new PawnMovesCalculator(myPieceColor, myPieceType);
                validMoves = Pawn.pieceMoves(board, myPosition);
        }
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return TeamColor == that.TeamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(TeamColor, type);
    }

    public String toString(){
        String string = String.format("Color = %s, type = %s", TeamColor, type);
        return string;
    }
}
