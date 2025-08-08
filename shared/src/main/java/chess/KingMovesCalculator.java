package chess;

import java.util.ArrayList;
import java.util.Collection;


public class KingMovesCalculator implements PieceMovesCalculator {

    public KingMovesCalculator() {

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        //create validMoves
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(up(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(down(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(left(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(right(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(upLeft(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(upRight(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(downLeft(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(downRight(myPosition, row, col, board, myPieceColor));
        return validMoves;
    }

    public Collection<ChessMove> up(ChessPosition startposition, int row, int col, ChessBoard board,
                                    ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (col + 1 <= 8 ){
            col += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> down(ChessPosition startposition, int row, int col, ChessBoard board,
                                      ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (col - 1 > 0 ){
            col -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> left(ChessPosition startposition, int row, int col, ChessBoard board,
                                      ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row  + 1 <= 8 ){
            row += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
        }

    public Collection<ChessMove> right(ChessPosition startposition, int row, int col, ChessBoard board,
                                       ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 ){
            row -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> upRight(ChessPosition startposition, int row, int col, ChessBoard board,
                                         ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row + 1 <= 8 & col + 1 <= 8 ){
            row += 1;
            col += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> upLeft(ChessPosition startposition, int row, int col, ChessBoard board,
                                        ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row + 1 <= 8 & col - 1 > 0 ){
            row += 1;
            col -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> downRight(ChessPosition startposition, int row, int col, ChessBoard board,
                                           ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 & col + 1 <= 8 ){
            row -= 1;
            col += 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }

    public Collection<ChessMove> downLeft(ChessPosition startposition, int row, int col, ChessBoard board,
                                          ChessGame.TeamColor teamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 & col - 1 > 0 ){
            row -= 1;
            col -= 1;
        }
        return addToBoard(row, col, teamColor, startposition, moves, board);
    }
}
