package chess;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

default Collection<ChessMove> AddToBoard(int row, int col, ChessGame.TeamColor TeamColor, ChessPosition startposition,
                        Collection<ChessMove> moves, ChessBoard board){
    ChessPosition newPosition = new ChessPosition(row, col);
    ChessPiece newPiece = board.getPiece(newPosition);
    moves = AddToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
    return moves;
}

    default Collection<ChessMove> AddToMoves(ChessPiece newPiece, ChessGame.TeamColor TeamColor, int row,
                                             int col, ChessPosition startposition, Collection<ChessMove> moves, ChessPosition newPosition) {
        //check if there is a piece on the square or not (null)
        if (newPiece != null) {
            ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
            //if piece, check if color is same or opposite
            if (TeamColor != newPieceColor) {
                //if piece is different color, capture it and move to newPosition
                moves = FinishMoves(startposition, newPosition, moves);
            }
        }
        else {
            //There is no piece, move it up to newPosition
            moves = FinishMoves(startposition, newPosition, moves);
        }
        return moves;
    }
    default Collection<ChessMove> FinishMoves(ChessPosition startPosition, ChessPosition finalPosition, Collection<ChessMove> moves){
        ChessMove chessmove = new ChessMove(startPosition, finalPosition, null);
        moves.add(chessmove);
        return moves;
    }
}
