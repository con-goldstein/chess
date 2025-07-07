package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator{
    public QueenMovesCalculator(){}

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        //piece color
        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();

        //moves
        up(myPosition, row, col, board, myPieceColor, validMoves);
        down(myPosition, row, col, board, myPieceColor, validMoves);
        left(myPosition, row, col, board, myPieceColor, validMoves);
        right(myPosition, row, col, board, myPieceColor, validMoves);
        upRight(myPosition, row, col, board, myPieceColor, validMoves);
        upLeft(myPosition, row, col, board, myPieceColor, validMoves);
        downRight(myPosition, row, col, board, myPieceColor, validMoves);
        downLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }
}