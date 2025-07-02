package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public BishopMovesCalculator(ChessGame.TeamColor TeamColor, ChessPiece.PieceType type) {
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // get row and col where piece is
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();

        //up right
        UpRight(myPosition, row, col, board, myPieceColor, validMoves);
        //up left
        UpLeft(myPosition, row, col, board, myPieceColor, validMoves);
        //up right
        BottomRight(myPosition, row, col, board, myPieceColor,validMoves);
        //bottom left
        BottomLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }
    public void UpRight(ChessPosition startposition, int row, int col,
                                         ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
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
        validMoves.addAll(moves);
    }
//    default Collection<ChessMove> AddToBoard(int row, int col, ChessGame.TeamColor TeamColor, ChessPosition startposition,
//                                             Collection<ChessMove> moves, ChessBoard board){
    public void UpLeft(ChessPosition startposition, int row, int col,
                        ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
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
        validMoves.addAll(moves);
    }

    public void BottomRight(ChessPosition startposition, int row, int col,
                        ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
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
        validMoves.addAll(moves);
    }
    public void BottomLeft(ChessPosition startposition, int row, int col,
                        ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
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
    validMoves.addAll(moves);    }
}