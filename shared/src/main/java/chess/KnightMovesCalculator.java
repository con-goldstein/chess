package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public KnightMovesCalculator(){}

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // get row and col where piece is
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();

        //up right
        validMoves.addAll(UpRightLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(UpRightShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(UpLeftLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(UpLeftShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(BottomRightLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(BottomRightShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(BottomLeftLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(BottomLeftShort(myPosition, row, col, board, myPieceColor));
        return validMoves;
    }

    public Collection<ChessMove> UpRightLong(ChessPosition startposition, int row, int col,
                                         ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 2 <= 8) & (col + 1 <= 8)){
            row += 2;
            col += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> UpRightShort(ChessPosition startposition, int row, int col,
                                         ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 1 <= 8) & (col + 2 <= 8)){
            row += 1;
            col += 2;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> UpLeftLong(ChessPosition startposition, int row, int col,
                                         ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 2 <= 8) & (col - 1 > 0)){
            row += 2;
            col -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> UpLeftShort(ChessPosition startposition, int row, int col,
                                            ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 1 <= 8) & (col - 2 > 0)){
            row += 1;
            col -= 2;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> BottomRightLong(ChessPosition startposition, int row, int col,
                                        ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 2 > 0) & (col + 1 <= 8)){
            row -= 2;
            col += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> BottomRightShort(ChessPosition startposition, int row, int col,
                                                 ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 1 > 0) & (col + 2 <= 8)){
            row -= 1;
            col += 2;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> BottomLeftLong(ChessPosition startposition, int row, int col,
                                        ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 2 > 0) & (col - 1 > 0)){
            row -= 2;
            col -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
    public Collection<ChessMove> BottomLeftShort(ChessPosition startposition, int row, int col,
                                                ChessBoard board, ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 1 > 0) & (col - 2 > 0)){
            row -= 1;
            col -= 2;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
}