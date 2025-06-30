package chess;

import java.util.ArrayList;
import java.util.Collection;


public class KingMovesCalculator implements PieceMovesCalculator {
    private ChessGame.TeamColor TeamColor;
    private ChessPiece.PieceType type;

    public KingMovesCalculator(ChessGame.TeamColor TeamColor, ChessPiece.PieceType type) {
        this.TeamColor = TeamColor;
        this.type = type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        //create validMoves
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(Up(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(Down(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(Left(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(Right(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(UpLeft(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(UpRight(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(DownLeft(myPosition, row, col, board, myPieceColor));
        validMoves.addAll(DownRight(myPosition, row, col, board, myPieceColor));
        return validMoves;
    }

    public Collection<ChessMove> Up(ChessPosition startposition, int row, int col, ChessBoard board,
                                    ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (col + 1 <= 8 ){
            col += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> Down(ChessPosition startposition, int row, int col, ChessBoard board,
                                    ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (col - 1 > 0 ){
            col -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> Left(ChessPosition startposition, int row, int col, ChessBoard board,
                                    ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row  + 1 < 8 ){
            row += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
        }

    public Collection<ChessMove> Right(ChessPosition startposition, int row, int col, ChessBoard board,
                                    ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 ){
            row -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> UpRight(ChessPosition startposition, int row, int col, ChessBoard board,
                                       ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row + 1 <= 8 & col + 1 <= 8 ){
            row += 1;
            col += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> UpLeft(ChessPosition startposition, int row, int col, ChessBoard board,
                                         ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row + 1 <= 8 & col - 1 > 0 ){
            row += 1;
            col -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> DownRight(ChessPosition startposition, int row, int col, ChessBoard board,
                                         ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 & col + 1 <= 8 ){
            row -= 1;
            col += 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }

    public Collection<ChessMove> DownLeft(ChessPosition startposition, int row, int col, ChessBoard board,
                                         ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        if (row - 1 > 0 & col - 1 > 0 ){
            row -= 1;
            col -= 1;
        }
        return AddToBoard(row, col, TeamColor, startposition, moves, board);
    }
}
