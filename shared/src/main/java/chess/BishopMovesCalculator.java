package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private ChessGame.TeamColor TeamColor;
    private ChessPiece.PieceType type;
    public BishopMovesCalculator(ChessGame.TeamColor TeamColor, ChessPiece.PieceType type) {
        this.TeamColor = TeamColor;
        this.type = type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // get row and col where piece is
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // get piece type & color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();

        Collection<ChessMove> validMoves = new ArrayList<>();
        //up right
        validMoves.addAll(UpRight(myPosition, row, col, board, myPieceColor));
        //up left
        validMoves.addAll(UpLeft(myPosition, row, col, board, myPieceColor));
        //up right
        validMoves.addAll(BottomRight(myPosition, row, col, board, myPieceColor));
        //bottom left
        validMoves.addAll(BottomLeft(myPosition, row, col, board, myPieceColor));
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
}