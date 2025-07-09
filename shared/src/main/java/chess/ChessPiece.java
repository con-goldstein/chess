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
private ChessGame.TeamColor teamColor;
private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor teamColor, ChessPiece.PieceType type) {
        this.teamColor = teamColor;
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
        return teamColor;
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

        // get piece type & color
        ChessPiece.PieceType myPieceType = board.getPiece(myPosition).getPieceType();

        Collection<ChessMove> validMoves = new ArrayList<>();
        //get pieceMoves for each type of piece
        switch(myPieceType){
            case BISHOP:
                BishopMovesCalculator bishop = new BishopMovesCalculator();
                validMoves = bishop.pieceMoves(board, myPosition);
                break;
            case KING:
                KingMovesCalculator king = new KingMovesCalculator();
                validMoves = king.pieceMoves(board, myPosition);
                break;
            case KNIGHT:
                KnightMovesCalculator knight = new KnightMovesCalculator();
                validMoves = knight.pieceMoves(board, myPosition);
                break;
            case PAWN:
                PawnMovesCalculator pawn = new PawnMovesCalculator();
                validMoves = pawn.pieceMoves(board, myPosition);
                break;
            case QUEEN:
                QueenMovesCalculator queen = new QueenMovesCalculator();
                validMoves = queen.pieceMoves(board, myPosition);
                break;
            case ROOK:
                RookMovesCalculator rook = new RookMovesCalculator();
                validMoves = rook.pieceMoves(board, myPosition);
                break;
}
        return validMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }

    public String toString(){
        String string = String.format("Color = %s, type = %s", teamColor, type);
        return string;
    }
}
