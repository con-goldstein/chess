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
                                                      ChessGame.TeamColor TeamColor) {
        Collection<ChessMove> moves = new ArrayList<>();

        if (TeamColor == ChessGame.TeamColor.WHITE) {
            // forward one or two spaces
            if (row == 2) {
                if (board.getPiece(new ChessPosition(3, col)) == null) {
                    if (board.getPiece(new ChessPosition(4, col)) == null) {
                        moves = PawnAddBoard(4, col, myposition, moves, board, null);
                    }
                }
            }
            row += 1;
            if (row == 8) {
                moves = PawnAddBoard(row, col, myposition, moves, board, QUEEN);
                moves = PawnAddBoard(row, col, myposition, moves, board, BISHOP);
                moves = PawnAddBoard(row, col, myposition, moves, board, ROOK);
                moves = PawnAddBoard(row, col, myposition, moves, board, KNIGHT);
            } else {
                moves = PawnAddBoard(row, col, myposition, moves, board, null);
            }

            // capture diagonally
            int captureRow = myposition.getRow() + 1;
            int[] captureCols = {col - 1, col + 1};
            for (int c : captureCols) {
                if (OnBoard(captureRow, c)) {
                    ChessPosition newPos = new ChessPosition(captureRow, c);
                    ChessPiece target = board.getPiece(newPos);
                    if (target != null && target.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        addCaptureMove(myposition, newPos, moves);
                    }
                }
            }
        }

        if (TeamColor == ChessGame.TeamColor.BLACK) {
            // forward one or two spaces
            if (row == 7) {
                if (board.getPiece(new ChessPosition(6, col)) == null){
                    if (board.getPiece(new ChessPosition(5, col)) == null){
                        moves = PawnAddBoard(5, col, myposition, moves, board, null);
                    }
                }
            }
            row -= 1;
            if (row == 1) {
                moves = PawnAddBoard(row, col, myposition, moves, board, QUEEN);
                moves = PawnAddBoard(row, col, myposition, moves, board, BISHOP);
                moves = PawnAddBoard(row, col, myposition, moves, board, ROOK);
                moves = PawnAddBoard(row, col, myposition, moves, board, KNIGHT);
            } else {
                moves = PawnAddBoard(row, col, myposition, moves, board, null);
            }

            // capture diagonally
            int captureRow = myposition.getRow() - 1;
            int[] captureCols = {col - 1, col + 1};
            for (int c : captureCols) {
                if (OnBoard(captureRow, c)) {
                    ChessPosition newPos = new ChessPosition(captureRow, c);
                    ChessPiece target = board.getPiece(newPos);
                    if (target != null && target.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        addCaptureMove(myposition, newPos, moves);
                    }
                }
            }
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

    public boolean OnBoard(int row, int col){
        if ((row >= 1) & (row <= 8) & (col >= 1) & (col <= 8)){
            return true;
        }
        else{
            return false;
        }
    }
    private void addCaptureMove(ChessPosition start, ChessPosition end, Collection<ChessMove> moves) {
        int endRow = end.getRow();
        if (endRow == 8 || endRow == 1) {
            // Promotion capture
            moves.add(new ChessMove(start, end, QUEEN));
            moves.add(new ChessMove(start, end, BISHOP));
            moves.add(new ChessMove(start, end, ROOK));
            moves.add(new ChessMove(start, end, KNIGHT));
        } else {
            // Normal capture
            moves.add(new ChessMove(start, end, null));
        }
    }
}