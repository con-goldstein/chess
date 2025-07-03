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
        UpRight(myPosition, row, col, board, myPieceColor, validMoves);
        // up left
        UpLeft(myPosition, row, col, board, myPieceColor, validMoves);
        // down right
        DownRight(myPosition, row, col, board, myPieceColor, validMoves);
        // down left
        DownLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }
}