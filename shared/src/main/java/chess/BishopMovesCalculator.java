package chess;
import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public BishopMovesCalculator(){}
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        ChessGame.TeamColor myPieceColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();

        //up right
        upRight(myPosition, row, col, board, myPieceColor, validMoves);
        // up left
        upLeft(myPosition, row, col, board, myPieceColor, validMoves);
        // down right
        downRight(myPosition, row, col, board, myPieceColor, validMoves);
        // down left
        downLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }
}