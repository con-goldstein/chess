package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private final ChessBoard board;
    public static boolean whitePlayer;
    Collection<ChessMove> moves;

public ChessBoardUI(ChessBoard board){
    this.board = (board != null) ? board : new ChessGame().getBoard();
}


public static void main(String[] args) {
    ChessBoard board = new ChessGame().getBoard();
    ChessBoardUI ui = new ChessBoardUI(board);
    ui.run("BLACK");
}

public void run(String color){
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    if (color.equals("WHITE")){
        whitePlayer = true;
    }
    else{
        whitePlayer = false;}

    out.println();

    drawHeaders(out);

    drawChessBoard(out);

    drawHeaders(out);

}

    private static void drawHeaders(PrintStream out) {
        String[] headers;
        out.print("   ");
        if (whitePlayer) {
            headers = new String[]{"  a ", "  b  ", "  c  ", "  d  ", "  e  ", "  f  ", "  g  ", "  h  "};
        }
        else {
            headers = new String[]{"  h  ", "  g  ", "  f  ", "  e  ", "  d  ", "  c  ", "  b  ", "  a  "};
        }
        for (String header : headers) {
            drawHeader(out, header);
        }

        out.println(RESET_BG_COLOR);
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(headerText);
    }

    private void drawChessBoard(PrintStream out) {
    if (!whitePlayer){
        for (int boardRow = 1; boardRow <= 8; boardRow++) {
            drawColumnHeader(out, boardRow);
            drawRowOfSquares(out, boardRow);
            drawColumnHeader(out, boardRow);
            out.println(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
        }
    }
    else{
        for (int boardRow = 8; boardRow >= 1; boardRow--){
            drawColumnHeader(out, boardRow);
            drawRowOfSquares(out, boardRow);
            drawColumnHeader(out, boardRow);
            out.println(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
        }
    }
    }

    private void drawRowOfSquares(PrintStream out, int boardRow) {
        if (whitePlayer) {
            for (int boardCol = 1; boardCol <= 8; boardCol++) {
                drawSquare(out, boardRow, boardCol);
            }
        } else {
            for (int boardCol = 8; boardCol >= 1; boardCol--) {
                drawSquare(out, boardRow, boardCol);
            }
        }
    }


    private void drawSquare(PrintStream out, int row, int col){
    boolean isWhite = (row + col) % 2 == 1;
    boolean highlighted = false;
    //set square to white or black
        if (isWhite){
            ChessPosition pos = new ChessPosition(row, col);
            if (moves != null){
                for (ChessMove move : moves){
                    ChessPosition highlightedPos = move.getEndPosition();
                    if (pos.equals(highlightedPos)){
                        highlighted = true;
                    }
                }
            }
            if (highlighted){
                out.print(SET_BG_COLOR_YELLOW);
            }
            else {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            }
        }
        else {
            ChessPosition pos = new ChessPosition(row, col);
            if (moves != null){
                for (ChessMove move : moves){
                    ChessPosition highlightedPos = move.getEndPosition();
                    if (pos.equals(highlightedPos)){
                        highlighted = true;
                    }
                }
            }
            if (highlighted){
                out.print(SET_BG_COLOR_YELLOW);
            }
            else {
                out.print(SET_BG_COLOR_DARK_GREEN);
            }
        }
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece != null){
            ChessPiece.PieceType type = piece.getPieceType();
            ChessGame.TeamColor color = piece.getTeamColor();
            String symbol = getsymbol(type, color);
            out.print(" " + symbol + " ");
        }
        else {
            out.print("     ");
        }
    }
    public static String getsymbol(ChessPiece.PieceType type, ChessGame.TeamColor color){
        switch (color){
            case WHITE:
                return switch (type) {
                    case KING -> BLACK_KING;
                    case QUEEN -> BLACK_QUEEN;
                    case BISHOP -> BLACK_BISHOP;
                    case KNIGHT -> BLACK_KNIGHT;
                    case ROOK -> BLACK_ROOK;
                    case PAWN -> BLACK_PAWN;
                };
            case BLACK:
                return switch (type) {
                    case KING -> WHITE_KING;
                    case QUEEN -> WHITE_QUEEN;
                    case BISHOP -> WHITE_BISHOP;
                    case KNIGHT -> WHITE_KNIGHT;
                    case ROOK -> WHITE_ROOK;
                    case PAWN -> WHITE_PAWN;
                };
        }
        return " ";
    }


    private void drawColumnHeader(PrintStream out, int boardRow){
        out.print(SET_BG_COLOR_DARK_GREY);
        String symbol;
        if (whitePlayer) {
            symbol = Integer.toString(Math.abs(boardRow));
        }
        else{
            symbol = Integer.toString(Math.abs(boardRow));
        }
        out.print(SET_TEXT_COLOR_MAGENTA + " " + symbol + " " + RESET_TEXT_COLOR);
    }

    public void highlight(Collection<ChessMove> validMoves, boolean whitePlayer) {
        for (ChessMove move : validMoves) {
            ChessPosition position = move.getEndPosition();
//            int row = position.getRow();
//            int col = position.getColumn();
            moves = validMoves;
        }
        if (whitePlayer) {
            run("WHITE");
        } else {
            run("BLACK");
        }
    }
}
