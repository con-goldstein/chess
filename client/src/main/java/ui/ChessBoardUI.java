package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    //board dimension
    private static final int BOARD_SIZE = 9;
    private static final int SQUARE_SIZE = 1;
    private static final int LINE_WIDTH = 1;
    private final ChessBoard board;
    public static boolean whitePlayer;

private static final String EMPTY = " ";
public ChessBoardUI(ChessBoard board){
    this.board = (board != null) ? board : new ChessGame().getBoard();
}


public static void main(String[] args) {
    ChessBoard board = new ChessGame().getBoard();
    ChessBoardUI ui = new ChessBoardUI(board);
    ui.run("WHITE");
}

public void run(String color){
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    if (color.equals("WHITE")){
        whitePlayer = true;
    }
    else{
        whitePlayer = false;}

    out.print(ERASE_SCREEN);

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
    printHeaderText(out, headerText);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_TEXT_COLOR_MAGENTA);
        out.print(player);
    }

    private void drawChessBoard(PrintStream out) {
    if (whitePlayer){
        for (int boardRow = 1; boardRow <= 8; boardRow++) {
            drawColumnHeader(out, boardRow);
            drawRowOfSquares(out, boardRow);
            drawColumnHeader(out, boardRow);
            out.println(RESET_BG_COLOR);
        }
    }
    else{
        for (int boardRow = 8; boardRow >= 1; boardRow--){
            drawColumnHeader(out, boardRow);
            drawRowOfSquares(out, boardRow);
            drawColumnHeader(out, boardRow);
            out.println(RESET_BG_COLOR);
        }
    }
    }

    private void drawRowOfSquares(PrintStream out, int boardRow){
            for (int boardCol = 1; boardCol < BOARD_SIZE; boardCol++) {
                drawSquare(out, boardRow, boardCol);
            }
        }


    private void drawSquare(PrintStream out, int row, int col){
    boolean isWhite = (row + col) % 2 == 0;
    //set square to white or black
        if (isWhite){
            out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        else {
            out.print(SET_BG_COLOR_DARK_GREEN);
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
                    case KING -> WHITE_KING;
                    case QUEEN -> WHITE_QUEEN;
                    case BISHOP -> WHITE_BISHOP;
                    case KNIGHT -> WHITE_KNIGHT;
                    case ROOK -> WHITE_ROOK;
                    case PAWN -> WHITE_PAWN;
                };
            case BLACK:
                return switch (type) {
                    case KING -> BLACK_KING;
                    case QUEEN -> BLACK_QUEEN;
                    case BISHOP -> BLACK_BISHOP;
                    case KNIGHT -> BLACK_KNIGHT;
                    case ROOK -> BLACK_ROOK;
                    case PAWN -> BLACK_PAWN;
                };
        }
        return " ";
    }


    private void printPlayer(PrintStream out, String symbol) {
        //if white, set text to black
        //if black, set text to white
//            if (whitePlayer) {
//                out.print(SET_TEXT_COLOR_WHITE);
//            } else {
//                out.print(SET_TEXT_COLOR_BLACK);
//        }

        out.print(" " + symbol + " ");
    }

    private void drawColumnHeader(PrintStream out, int boardRow){
        out.print(SET_BG_COLOR_DARK_GREY);
        String symbol;
        if (whitePlayer) {
            symbol = Integer.toString(Math.abs(9 - boardRow));
        }
        else{
            symbol = Integer.toString(Math.abs(boardRow - 9));
        }
        out.print(SET_TEXT_COLOR_MAGENTA + " " + symbol + " " + RESET_TEXT_COLOR);
    }




}
