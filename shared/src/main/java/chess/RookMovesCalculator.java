package chess;
import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    public RookMovesCalculator(){}
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();
        //up
        up(myPosition, row, col, board,teamColor, validMoves);
        //down
        down(myPosition, row, col, board,teamColor, validMoves);
        //left
        left(myPosition, row, col, board,teamColor, validMoves);
        //right
        right(myPosition, row, col, board,teamColor, validMoves);
        return validMoves;
    }
}