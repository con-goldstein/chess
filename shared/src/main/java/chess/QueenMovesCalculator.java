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
        Up(myPosition, row, col, board, myPieceColor, validMoves);
        Down(myPosition, row, col, board, myPieceColor, validMoves);
        Left(myPosition, row, col, board, myPieceColor, validMoves);
        Right(myPosition, row, col, board, myPieceColor, validMoves);
        UpRight(myPosition, row, col, board, myPieceColor, validMoves);
        UpLeft(myPosition, row, col, board, myPieceColor, validMoves);
        DownRight(myPosition, row, col, board, myPieceColor, validMoves);
        DownLeft(myPosition, row, col, board, myPieceColor, validMoves);
        return validMoves;
    }
}