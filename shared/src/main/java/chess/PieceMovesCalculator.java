package chess;
import java.util.ArrayList;
import java.util.Collection;

interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);

default Collection<ChessMove> addToBoard(int row, int col, ChessGame.TeamColor TeamColor, ChessPosition startposition,
                                         Collection<ChessMove> moves, ChessBoard board){
    ChessPosition newPosition = new ChessPosition(row, col);
    ChessPiece newPiece = board.getPiece(newPosition);
    moves = addToMoves(newPiece, TeamColor, row, col, startposition, moves, newPosition);
    return moves;
}

    default Collection<ChessMove> addToMoves(ChessPiece newPiece, ChessGame.TeamColor TeamColor, int row, int col,
                                             ChessPosition startposition, Collection<ChessMove> moves, ChessPosition newPosition) {
        if (newPiece != null){
            ChessGame.TeamColor newPieceColor = newPiece.getTeamColor();
            //if piece != null, check if color is opposite
            if (TeamColor != newPieceColor){
                moves = finishMoves(startposition, newPosition, moves);
            }
        }
        else{
            moves = finishMoves(startposition, newPosition, moves);
        }
        return moves;
    }

    default Collection<ChessMove> finishMoves(ChessPosition startPosition, ChessPosition finalPosition,
                                              Collection<ChessMove> moves){
        ChessMove chessmove = new ChessMove(startPosition, finalPosition, null);
        moves.add(chessmove);
        return moves;
    }
    default void up(ChessPosition myPosition, int row, int col, ChessBoard board,
                    ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (row + 1 <= 8){
            row += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void down(ChessPosition myPosition, int row, int col, ChessBoard board,
                      ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (row - 1 >= 1){
            row -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void left(ChessPosition myPosition, int row, int col, ChessBoard board,
                      ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (col - 1 >= 1){
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void right(ChessPosition myPosition, int row, int col, ChessBoard board,
                       ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while (col + 1 <= 8){
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void upRight(ChessPosition myPosition, int row, int col, ChessBoard board,
                         ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row + 1 <= 8) & (col + 1 <= 8)){
            row += 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void upLeft(ChessPosition myPosition, int row, int col, ChessBoard board,
                        ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row + 1 <= 8) & (col - 1 >= 1)){
            row += 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void downRight(ChessPosition myPosition, int row, int col, ChessBoard board,
                           ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row - 1 >= 1) & (col + 1 <= 8)){
            row -= 1;
            col += 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
    default void downLeft(ChessPosition myPosition, int row, int col, ChessBoard board,
                          ChessGame.TeamColor TeamColor, Collection<ChessMove> validMoves){
        Collection<ChessMove> moves = new ArrayList<>();
        while ((row - 1 >= 1) & (col - 1 >= 1)){
            row -= 1;
            col -= 1;
            ChessPosition newPosition = new ChessPosition(row, col);
            ChessPiece newPiece = board.getPiece(newPosition);
            moves = addToMoves(newPiece, TeamColor, row, col, myPosition, moves, newPosition);
            if (newPiece != null){
                break;
            }
        }
        validMoves.addAll(moves);
    }
}