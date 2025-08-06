import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import exceptions.DataAccessException;
import model.*;
import org.eclipse.jetty.server.Authentication;
import ui.ChessBoardUI;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Integer.parseInt;
import static ui.EscapeSequences.*;


public class InGame implements ServerMessageObserver {
    public final Scanner scanner = new Scanner(System.in);
    websocket.WebSocketFacade ws;
    GameData gameData;
    ChessGame game;

    public InGame() throws Exception {
        ws = new WebSocketFacade(this);
    }


    public void run(HashMap<Integer, GameData> gamesMap, String[] splitResult, String authToken, String username) throws IOException, DataAccessException {
        int gameID = parseInt(splitResult[1]);
        GameData game = gamesMap.get(gameID);
        String action = splitResult[0];
        //connect to server
        ws.connectToServer(authToken, gameID);

        String helpLine = "help - with possible commands\n" +
                "redraw - to redraw game board\n" +
                "leave - to leave the game\n" +
                "move <piece> <move>  - to make a move\n" +
                "resign - to resign from the game" +
                "highlight <piece> - to highlight all legal moves on chessboard for a piece";
        System.out.println(RESET_TEXT_COLOR + helpLine);
        System.out.print(SET_TEXT_COLOR_GREEN + " >>> " + RESET_TEXT_COLOR);
        String[] result = scanner.nextLine().split(" ");
        String keyword = result[0];

        while (true) {
            switch (keyword){
                case "help":
                    System.out.println(helpLine);
                    System.out.print(SET_TEXT_COLOR_GREEN + ">>> " + RESET_TEXT_COLOR);
                    break;
                case "redraw":
                    redraw(game, action, splitResult);
                    break;
                case "leave":
                    ws.leave(authToken, gameID);
                    break;
                case "move":
                    if (splitResult.length != 3){
                        System.out.println("Please enter keyword, with current piece position and where you would like it to go");
                        break;
                    }
                    //need to create new chessmove to put into makeMove object

                case "resign":
                    System.out.println("Are you sure you want to resign? (Y/N)");
                    String newResult = scanner.nextLine().toLowerCase();
                    if (newResult.equals("y")){
                        ws.resign(authToken, gameID);
                        break;
                    }
                    break;
                case "highlight":
                    if (result.length != 2){
                        System.out.println("Please input highlight and then piece you would like to see");
                    }
                    else{

                    }
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
        ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
        switch (msg.getServerMessageType()){
            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                System.out.println(SET_TEXT_COLOR_RED + errorMessage.toString() + RESET_TEXT_COLOR);
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                System.out.println(SET_TEXT_COLOR_BLUE + notificationMessage.toString() + RESET_TEXT_COLOR);
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                System.out.println(loadGameMessage.toString());
        }
    }

//    public ChessPiece getPiece(String result){
//
//    }
}
