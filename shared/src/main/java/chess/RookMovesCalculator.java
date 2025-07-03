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
        Up(myPosition, row, col, board,TeamColor, validMoves);
        //down
        Down(myPosition, row, col, board,TeamColor, validMoves);
        //left
        Left(myPosition, row, col, board,TeamColor, validMoves);
        //right
        Right(myPosition, row, col, board,TeamColor, validMoves);
        return validMoves;
    }
}