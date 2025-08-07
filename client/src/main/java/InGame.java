import chess.*;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exceptions.DataAccessException;
import model.*;
import ui.ChessBoardUI;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;


public class InGame implements ServerMessageObserver {
    public final Scanner scanner = new Scanner(System.in);
    websocket.WebSocketFacade ws;
    boolean whitePlayer;
    ChessGame game;
    GameData gameData;
    public InGame() throws Exception {
        ws = new WebSocketFacade(this);
    }


    public void run(HashMap<Integer, GameData> gamesMap, String[] splitResult, String authToken, String username)
            throws IOException, DataAccessException {
        int hashMapKey = parseInt(splitResult[1]);
        if (hashMapKey == -1){
            System.out.println("gameid == -1, game not found");
        }
        gameData = gamesMap.get(hashMapKey);
        if (gameData == null){
            System.out.println(RESET_TEXT_COLOR + "please list the games before joining");
            return;
        }
        game = gameData.game();
        int gameID = gameData.gameID();
        ChessBoard board = game.getBoard();
        String action = splitResult[0];
        //set if user is playing white or black
        if (username.equals(gameData.whiteUsername())){
            whitePlayer = true;
        }
        else {
            whitePlayer = false;
        }
        //connect to server
        ws.connectToServer(authToken, gameID);

        String helpLine = "help - with possible commands\n" +
                "redraw - to redraw game board\n" +
                "leave - to leave the game\n" +
                "move <starting position> <ending position> - to make a move\n" +
                "resign - to resign from the game\n" +
                "highlight <piece position> - to highlight all legal moves on chessboard for a piece";
        System.out.println(RESET_TEXT_COLOR + helpLine);
        while (true) {
            System.out.print(SET_TEXT_COLOR_GREEN + " >>> " + RESET_TEXT_COLOR);
            String[] result = scanner.nextLine().split(" ");
            String keyword = result[0];
            switch (keyword){
                case "help":
                    System.out.println(helpLine);
                    break;
                case "redraw":
                    redraw(gameData, action, splitResult);
                    break;
                case "leave":
                    ws.leave(authToken, gameID);
                    return;
                case "move":
                    if (result.length != 3){
                        System.out.println(SET_TEXT_COLOR_GREEN +
                                "Please enter keyword, with current piece position and where you would like it to go" + RESET_TEXT_COLOR);
                        break;
                    }
                    else{
                        ChessMove move = getMove(result, gameData);
                        ws.makeMove(authToken, gameID, move);
                        break;
                    }

                case "resign":
                    System.out.println(SET_TEXT_COLOR_GREEN + "Are you sure you want to resign? (Y/N)" + RESET_TEXT_COLOR);
                    String newResult = scanner.nextLine().toLowerCase();
                    if (newResult.equals("y")){
                        System.out.println(helpLine);
                        ws.resign(authToken, gameID);
                        break;
                    }
                    break;
                case "highlight":
                    if (result.length != 2){
                        System.out.println("Please input highlight and then piece you would like to see");
                    }
                    else{

                        String startPos = splitResult[1];
                        int startCol = findColumn(startPos.split("")[0]);
                        int startRow = parseInt(startPos.split("")[1]);
                        ChessPosition startPosition = new ChessPosition(startRow, startCol);
                        ChessPiece piece = board.getPiece(startPosition);
                        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
                        ChessBoardUI boardUI = new ChessBoardUI(game.getBoard());
                        boardUI.highlight(pieceMoves, whitePlayer);
                    }
                default:
                    System.out.println("Please choose an action");
            }
        }
    }


    public void redraw(GameData game, String action, String[] result){
        ChessBoardUI boardUI = new ChessBoardUI(game.game().getBoard());
        if (action.equals("observe")){
            boardUI.run("WHITE");
        }
        else {
            if (result[2].equals("WHITE")){
                boardUI.run("WHITE");
            }
            else if (result[2].equals("BLACK")){
                boardUI.run("BLACK");
            }
            else{
                System.out.println("Error");
            }
        }
    }
    public void notify(String message){
        try {
            ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
            switch (msg.getServerMessageType()){
                case ERROR:
                    ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                    System.out.println(SET_TEXT_COLOR_RED + errorMessage.getMessage() + RESET_TEXT_COLOR);
                    break;
                case NOTIFICATION:
                    NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                    System.out.println(SET_TEXT_COLOR_BLUE + notificationMessage.getMessage() + RESET_TEXT_COLOR);
                    break;
                case LOAD_GAME:
                    LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
//                    System.out.println(loadGameMessage.getGame());
                    break;
            }
        } catch (JsonSyntaxException e) {
            System.out.println(e.getMessage());
        }
    }

    public ChessMove getMove(String[] splitResult, GameData game){
        ChessMove move;

        String startPos = splitResult[1];
        int startCol = findColumn(startPos.split("")[0]);
        int startRow = parseInt(startPos.split("")[1]);
        ChessPosition startPosition = new ChessPosition(startRow, startCol);

        String endPos = splitResult[2];
        int endCol = findColumn(endPos.split("")[0]);
        int endRow = parseInt(endPos.split("")[1]);
        ChessPosition endPosition = new ChessPosition(endRow, endCol);

        //get piece at startPosition
        ChessPiece piece = game.game().getBoard().getPiece(startPosition);
        if (piece == null){
            System.out.println("Square is empty, please choose a square with pieces on it");
        }
        else{
            ChessPiece.PieceType type = piece.getPieceType();
            //if piece is a pawn and ending row is 8/1, ask for promotion
            if (type.equals(ChessPiece.PieceType.PAWN)){
                if ((whitePlayer) & (endRow == 8)){
                    System.out.println("What would you like to promote to?");
                    String response = scanner.nextLine().toLowerCase();
                    ChessPiece.PieceType promoPiece = findPromoPiece(response);
                    move = new ChessMove(startPosition, endPosition, promoPiece);
                    return move;
                }
                else if ((!whitePlayer) & (endRow == 1)){
                    System.out.println("What would you like to promote to?");
                    String response = scanner.nextLine().toLowerCase();
                    ChessPiece.PieceType promoPiece = findPromoPiece(response);
                    move = new ChessMove(startPosition, endPosition, promoPiece);
                    return move;
                }
            }
        }
        return new ChessMove(startPosition, endPosition, null);
    }

    public ChessPiece.PieceType findPromoPiece(String piece){
        switch (piece){
            case "queen" -> {return ChessPiece.PieceType.QUEEN;}
            case "rook" -> {return ChessPiece.PieceType.ROOK;}
            case "knight" -> {return ChessPiece.PieceType.KNIGHT;}
            case "bishop" -> {return ChessPiece.PieceType.BISHOP;}
        }
        return null;
    }

    private int findColumn(String col) {
        if (whitePlayer){
            switch (col){
                case "a" -> {return 1;}
                case "b" -> {return 2;}
                case "c" -> {return 3;}
                case "d" -> {return 4;}
                case "e" -> {return 5;}
                case "f" -> {return 6;}
                case "g" -> {return 7;}
                case "h" -> {return 8;}
            }
            return 0;
        }
        else {
            switch (col){
                case "a" -> {return 8;}
                case "b" -> {return 7;}
                case "c" -> {return 6;}
                case "d" -> {return 5;}
                case "e" -> {return 4;}
                case "f" -> {return 3;}
                case "g" -> {return 2;}
                case "h" -> {return 1;}
            }
            return 0;
        }
    }

}
