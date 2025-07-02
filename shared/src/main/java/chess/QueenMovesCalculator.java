package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {

    public QueenMovesCalculator() {
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // get row and col where piece is
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();
        //up
        Up(myPosition, row, col, board, myPieceColor, validMoves);
        //down
        Down(myPosition, row, col, board, myPieceColor, validMoves);
        //left
        Left(myPosition, row, col, board, myPieceColor, validMoves);
        Right(myPosition, row, col, board, myPieceColor, validMoves);
        UpRight(myPosition, row, col, board, myPieceColor, validMoves);
        UpLeft(myPosition, row, col, board, myPieceColor, validMoves);
        DownRight(myPosition, row, col, board, myPieceColor, validMoves);
        DownLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }

    public void Up(ChessPosition startposition, int row, int col,
                        ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while (row + 1 <= 8) {
            row += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    public void Down(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while (row - 1 >= 1) {
            row -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    public void Left(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while (col - 1 >= 1) {
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
    public void Right(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while (col + 1 <= 8) {
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
    public void UpRight(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while ((row + 1 <= 8) & (col + 1 <= 8)) {
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
    public void UpLeft(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while ((row + 1 <= 8) & (col - 1 >= 1)) {
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
    public void DownRight(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while ((row - 1 >= 1) & (col + 1 <= 8)) {
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
    public void DownLeft(ChessPosition startposition, int row, int col,
                   ChessBoard board, ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        //check to make sure we are in the board
        while ((row - 1 >= 1) & (col - 1 >= 1)) {
            row -= 1;
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
}