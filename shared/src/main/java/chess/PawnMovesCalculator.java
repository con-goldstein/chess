package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.*;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private ChessGame.TeamColor TeamColor;
    private ChessPiece.PieceType type;
    public PawnMovesCalculator(ChessGame.TeamColor TeamColor, ChessPiece.PieceType type) {
        this.TeamColor = TeamColor;
        this.type = type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece myPiece = board.getPiece(myPosition);
        // get piece color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        //create validMoves
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(moveStartingPosition(myPosition, row, col, board, myPieceColor));
        return validMoves;
    }

    public Collection<ChessMove> moveStartingPosition(ChessPosition myposition, int row, int col, ChessBoard board,
                                         ChessGame.TeamColor TeamColor){
        Collection<ChessMove> moves = new ArrayList<>();
        //if pawn is white
        if (TeamColor == ChessGame.TeamColor.WHITE){
            //if pawn is in starting position
            if (row == 2){
                row += 1;
                moves = PawnAddBoard(row, col, myposition, moves, board, null);
            }
            if (row == 7){
                row += 1;
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.QUEEN);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.BISHOP);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.ROOK);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.KNIGHT);
                return moves;
            }
            row += 1;
            moves = PawnAddBoard(row, col, myposition, moves, board, null);

        }
        if (TeamColor == ChessGame.TeamColor.BLACK) {
            if (row == 7){
                row -= 1;
                moves = PawnAddBoard(row, col, myposition, moves, board, null);
            }
            if (row == 2){
                row -= 1;
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.QUEEN);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.BISHOP);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.ROOK);
                moves = PawnAddBoard(row, col, myposition, moves, board, ChessPiece.PieceType.KNIGHT);
                return moves;
            }
            row -=1;
            moves = PawnAddBoard(row, col, myposition, moves, board, null);
        }
        return moves;
    }

    public Collection<ChessMove> PawnAddBoard(int row, int col, ChessPosition startposition,
                                             Collection<ChessMove> moves, ChessBoard board, ChessPiece.PieceType PromoPiece){
        ChessPosition newPosition = new ChessPosition(row, col);
        ChessPiece newPiece = board.getPiece(newPosition);
        //if there is a piece already
        if (newPiece == null){
             moves = FinishMoves(startposition, newPosition, moves, PromoPiece);
        }
        return moves;
    }

public Collection<ChessMove> FinishMoves(ChessPosition startPosition, ChessPosition finalPosition, Collection<ChessMove> moves,
                                         ChessPiece.PieceType PromoPiece){
    ChessMove chessmove = new ChessMove(startPosition, finalPosition, PromoPiece);
    moves.add(chessmove);
    return moves;
}
}