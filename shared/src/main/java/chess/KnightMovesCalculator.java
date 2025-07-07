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
        validMoves.addAll(upRightLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(upRightShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(upLeftLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(upLeftShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(bottomRightLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(bottomRightShort(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(bottomLeftLong(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(bottomLeftShort(myPosition, row, col, board, myPieceColor));
        return validMoves;
    }

    public Collection<ChessMove> upRightLong(ChessPosition startposition, int row, int col,
                                             ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 2 <= 8) & (col + 1 <= 8)){
            row += 2;
            col += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> upRightShort(ChessPosition startposition, int row, int col,
                                              ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 1 <= 8) & (col + 2 <= 8)){
            row += 1;
            col += 2;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> upLeftLong(ChessPosition startposition, int row, int col,
                                            ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 2 <= 8) & (col - 1 > 0)){
            row += 2;
            col -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> upLeftShort(ChessPosition startposition, int row, int col,
                                             ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row + 1 <= 8) & (col - 2 > 0)){
            row += 1;
            col -= 2;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> bottomRightLong(ChessPosition startposition, int row, int col,
                                                 ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 2 > 0) & (col + 1 <= 8)){
            row -= 2;
            col += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> bottomRightShort(ChessPosition startposition, int row, int col,
                                                  ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 1 > 0) & (col + 2 <= 8)){
            row -= 1;
            col += 2;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> bottomLeftLong(ChessPosition startposition, int row, int col,
                                                ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 2 > 0) & (col - 1 > 0)){
            row -= 2;
            col -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
    public Collection<ChessMove> bottomLeftShort(ChessPosition startposition, int row, int col,
                                                 ChessBoard board, ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if ((row - 1 > 0) & (col - 2 > 0)){
            row -= 1;
            col -= 2;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
}