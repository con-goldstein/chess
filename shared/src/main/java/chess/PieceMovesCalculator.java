package chess;
import java.util.ArrayList;
import java.util.Collection;

interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);


    default Collection<ChessMove> AddToMoves(ChessPiece newPiece, ChessGame.TeamColor TeamColor, int row, int col,
                                             ChessPosition startposition, Collection<ChessMove> moves, ChessPosition newPosition) {
        if (newPiece != null){
            ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
            //if piece != null, check if color is opposite
            if (TeamColor != newPieceColor){
                moves = FinishMoves(startposition, newPosition, moves);
            }
        }
        else{
            moves = FinishMoves(startposition, newPosition, moves);
        }
        return moves;
    }

    default Collection<ChessMove> FinishMoves(ChessPosition startPosition, ChessPosition finalPosition,
                                              Collection<ChessMove> moves){
        ChessMove chessmove = new ChessMove(startPosition, finalPosition, null);
        moves.add(chessmove);
        return moves;
    }
    default void Up(ChessPosition myPosition, int row, int col, ChessBoard board,
                    ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (row + 1 <= 8){
            row += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void Down(ChessPosition myPosition, int row, int col, ChessBoard board,
                      ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (row - 1 >= 1){
            row -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void Left(ChessPosition myPosition, int row, int col, ChessBoard board,
                      ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (col - 1 >= 1){
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void Right(ChessPosition myPosition, int row, int col, ChessBoard board,
                       ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (col + 1 <= 8){
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void UpRight(ChessPosition myPosition, int row, int col, ChessBoard board,
                         ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row + 1 <= 8) & (col + 1 <= 8)){
            row += 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void UpLeft(ChessPosition myPosition, int row, int col, ChessBoard board,
                        ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row + 1 <= 8) & (col - 1 >= 1)){
            row += 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void DownRight(ChessPosition myPosition, int row, int col, ChessBoard board,
                           ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row - 1 >= 1) & (col + 1 <= 8)){
            row -= 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void DownLeft(ChessPosition myPosition, int row, int col, ChessBoard board,
                          ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row - 1 >= 1) & (col - 1 >= 1)){
            row -= 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = AddToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
}