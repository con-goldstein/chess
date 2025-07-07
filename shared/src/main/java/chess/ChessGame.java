package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
//board
/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor TeamColor;
    private ChessBoard chessBoard;

    public ChessGame() {
        this.TeamColor = TeamColor;
        this.chessBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return TeamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        TeamColor = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece chessPiece = chessBoard.getPiece(startPosition);
        Collection<ChessMove> toRemove = new ArrayList<>();
        if (chessPiece == null){
            return toRemove;
        }
        Collection<ChessMove> moves = chessPiece.pieceMoves(chessBoard, startPosition);
        for (ChessMove move : moves){
            ChessPosition endPosition = move.getEndPosition();
            //make copy of board
            ChessBoard newBoard = chessBoard.copy();
            //make move
            newBoard.addPiece(endPosition, chessPiece);
            newBoard.addPiece(startPosition, null);
            //in check uses internal board (squares),
            // so make temp variable to hold original board & switch with board copy
            ChessBoard originalBoard = this.chessBoard;
            this.chessBoard = newBoard;
            ChessGame.TeamColor TeamColor = chessPiece.getTeamColor();
            //check if king isInCheck(TeamColor)
            if (isInCheck(TeamColor)){
                toRemove.add(move);
                this.chessBoard = originalBoard;
            }
            else{
                this.chessBoard = originalBoard;
            }
        }
        moves.removeAll(toRemove);
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece piece = chessBoard.getPiece(startPosition);
        ChessPiece.PieceType PromoPiece = move.getPromotionPiece();
        TeamColor currentTeam = getTeamTurn();
        int row = endPosition.getRow();

        //get valid moves for piece at startPosition
        try{
            //check if there is a piece & if it is the correct turn
            if (piece != null && currentTeam == piece.getTeamColor()) {
                Collection<ChessMove> validMoves = validMoves(startPosition);
                if (validMoves.stream().anyMatch(validMove -> validMove.getEndPosition().equals(endPosition))) {
                    //make the move
                    //if piece is not a pawn
                    if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                        chessBoard.addPiece(endPosition, piece);
                    }
                    //if piece is a pawn
                    else {
//                  if pawn is white and at end of board, change to promotion piece
                        if (currentTeam == TeamColor.WHITE && row == 8) {
                            ChessPiece promoPiece = new ChessPiece(currentTeam, PromoPiece);
                            chessBoard.addPiece(endPosition, promoPiece);
                        }
                        //if pawn is black and at end of board, change to promotion piece
                        else if (currentTeam == TeamColor.BLACK && row == 1) {
                            ChessPiece promoPiece = new ChessPiece(currentTeam, PromoPiece);
                            chessBoard.addPiece(endPosition, promoPiece);
                        } else {
                            chessBoard.addPiece(endPosition, piece);
                        }
                    }
                    //change original position to null
                    chessBoard.addPiece(startPosition, null);
//
//                    //check for checkmate/stalemate
//                    boolean checkmate = isInCheckmate(currentTeam);
//                    boolean stalemate = isInStalemate(currentTeam);

                        //change team color
                    if (currentTeam == TeamColor.WHITE) {
                        setTeamTurn(TeamColor.BLACK);
                    } else {
                        setTeamTurn((TeamColor.WHITE));
                    }
                } else {
                    throw new InvalidMoveException("Invalid move");
                }
            }
            else {throw new InvalidMoveException(("Made move out of turn"));}
        }
        catch (InvalidMoveException e){
            throw new InvalidMoveException("Invalid move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find king position
        ChessPosition kingPos = findKingPos(teamColor);
        //find all valid moves of opponent pieces
        Collection<ChessMove> allValidMoves = findallValidMoves(teamColor);

        //check if king's position is in endPosition of any piece
        return allValidMoves.stream().anyMatch(move -> move.getEndPosition().equals(kingPos));
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //have to be in check to be in checkmate
        if (!isInCheck(teamColor)){
            return false;
        }
        //loop through every position
        for (int i=1; i <= 8; i++){
            for (int j=1; j<=8; j++){
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = chessBoard.getPiece(pos);
                //if there is a piece of teamColor
                if (piece != null && piece.getTeamColor() == teamColor){
                    //if piece has any valid moves, can get out of check and are not in checkmate
                    Collection<ChessMove> validMoves = validMoves(pos);
                    if (!validMoves.isEmpty()){
                        return false;
                    }

                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }
    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    public ChessPosition findKingPos(TeamColor teamColor){
        ChessPosition kingPos = null;
        //find king and it's valid moves
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (chessBoard.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                        ChessPosition pos = new ChessPosition(i, j);
                        ChessPiece king = chessBoard.getPiece(pos);
                        if (king.getTeamColor() == teamColor) {
                            kingPos = pos;
                        }
                    }
                }
            }
        }
        return kingPos;
    }

    public Collection<ChessMove> findallValidMoves(TeamColor teamColor){
        Collection<ChessMove> allValidMoves = new ArrayList<>();
        // loop through every piece on the board
        for (int i=1; i<=8; i++){
            for (int j=1; j<=8; j++){
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                // if we found a piece and it is opposite color
                if (piece != null && piece.getTeamColor() != teamColor){
                    //get piece moves
                    Collection<ChessMove> pieceMoves = piece.pieceMoves(chessBoard, new ChessPosition(i,j));
                    allValidMoves.addAll(pieceMoves);
                }
            }
        }
        return allValidMoves;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return TeamColor == chessGame.TeamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(TeamColor);
    }
}


