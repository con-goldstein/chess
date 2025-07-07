package chess;
import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    public RookMovesCalculator(){}
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor TeamColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();
        //up
        up(myPosition, row, col, board,TeamColor, validMoves);
        //down
        down(myPosition, row, col, board,TeamColor, validMoves);
        //left
        left(myPosition, row, col, board,TeamColor, validMoves);
        //right
        right(myPosition, row, col, board,TeamColor, validMoves);
        return validMoves;
    }
}