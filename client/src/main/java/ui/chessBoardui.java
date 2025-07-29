package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class chessBoardui {
    //board dimension
    private static final int boardSize = 8;
    private static final int squareSize = 1;
    private static final int lineWidth = 1;
    ChessBoard board;

private static final String EMPTY = " ";
public chessBoardui(){
    this.board = new ChessGame().getBoard();
}


public static void main(String[] args) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    out.print(ERASE_SCREEN);

    drawHeaders(out);

    drawChessBoard(out);

    drawHeaders(out);

    out.print(SET_BG_COLOR_BLACK);
    out.print(SET_TEXT_COLOR_WHITE);
}

    private static void drawHeaders(PrintStream out) {

        setBlack(out);

        String[] headers = { " ", "h", "g", "f", "e", "d", "c", "b", "a", " " };
        for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
            drawHeader(out, headers[boardCol]);

            if (boardCol < boardSize - 1) {
                out.print(EMPTY.repeat(lineWidth));
            }
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = squareSize / 2;
        int suffixLength = squareSize - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_MAGENTA);

        out.print(player);

        setBlack(out);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < boardSize; ++boardRow) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol){
                drawSquare(out, boardRow, boardCol);
            }

            drawRowOfSquares(out);

            if (boardRow < boardSize - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }
    private static void drawSquare(PrintStream out, int row, int col){

    }

    private static void drawRowOfSquares(PrintStream out) {

        for (int squareRow = 0; squareRow < squareSize; ++squareRow) {
            for (int boardCol = 0; boardCol < boardSize; ++boardCol) {
                setWhite(out);

                if (squareRow == squareSize / 2) {
                    setBlack(out);
                    int prefixLength = squareSize / 2;
                    int suffixLength = squareSize - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, WHITE_KING);
                    out.print(EMPTY.repeat(suffixLength));
                }
                else {
                    out.print(EMPTY.repeat(squareSize));
                }

                if (boardCol < boardSize - 1) {
                    // Draw vertical column separator.
                    setRed(out);
                    out.print(EMPTY.repeat(lineWidth));
                }

                setBlack(out);
            }

            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out) {

        int boardSizeInSpaces = boardSize * squareSize +
                (boardSize - 1) * lineWidth;

        for (int lineRow = 0; lineRow < lineWidth; ++lineRow) {
            setRed(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));

            setBlack(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void setDarkGrey(PrintStream out){
    out.print(SET_BG_COLOR_DARK_GREY);
    out.print(SET_TEXT_COLOR_DARK_GREY);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

}
