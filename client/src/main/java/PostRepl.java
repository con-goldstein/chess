import websocket.WebSocketFacade;
import chess.ChessGame;
import server.ServerFacade;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.GameData;
import requests.*;
import results.*;
import ui.ChessBoardUI;

import java.io.IOException;
import java.util.*;

import static ui.EscapeSequences.*;

public class PostRepl{
ServerFacade server;
PreRepl preRepl;
HashMap<Integer, GameData> gamesMap = new HashMap<>();
Repl repl;
InGame inGameClient;
public boolean inGame = false;
public PostRepl(ServerFacade serverFacade, PreRepl preRepl, Repl repl){
    this.server = serverFacade;
    this.preRepl = preRepl;
    this.repl = repl;
//    inGameClient = new InGame(gamesMap);
}

public void postEval(String username) throws BadRequestException, AlreadyTakenException,
        UnauthorizedException, DataAccessException, IOException {
    String helpLine = "create <Name> - a game\n" +
            "list - game\njoin <ID> [WHITE/BLACK] - a game\nobserve <ID> - a game\nlogout- when you are done\n" +
            "quit - playing chess\nhelp - with possible commands";
    System.out.println(RESET_TEXT_COLOR + helpLine);
    Scanner scanner = repl.scanner;
    while (true) {
        System.out.print(SET_TEXT_COLOR_GREEN + ">>> " + RESET_TEXT_COLOR);
        String result = scanner.nextLine();
        var splitResult = result.split(" ");
        String keyWord = splitResult[0];
        if (keyWord.equals("quit")) {
            repl.loggedIn = false;
            return;
        }
            switch (keyWord) {
                case "help":
                    System.out.println(helpLine);
                    break;
                case "logout":
                        server.logout();
                        System.out.println("logged out successfully\n" + RESET_TEXT_COLOR);
                        repl.loggedIn = false;
                        return;
                case "create":
                    createGame(splitResult, server);
                    break;
                case "list":
                    listGames(server, gamesMap);
                    break;
                case "join":
                    joinGame(splitResult, server, gamesMap, inGame);
                    inGameClient.run(gamesMap, splitResult, server.authToken, username);
                    break;
                case "observe":
                    observeGame(splitResult, gamesMap);
                    inGameClient.run(gamesMap, splitResult, server.authToken, username);
                    break;
                default:
                    System.out.println("Please input valid action");
            }
    }
}

public static void observeGame(String[] splitResult, HashMap<Integer, GameData> gamesMap){
    if (splitResult.length == 2) {
        try {
            int gameNumber = Integer.parseInt(splitResult[1]);
            if (gamesMap.containsKey(gameNumber)) {
                gamesMap.get(gameNumber).gameID();
                System.out.println("game is observable");
                ChessGame game = gamesMap.get(gameNumber).game();
                ChessBoardUI chessBoard = new ChessBoardUI(game.getBoard());
                chessBoard.run("WHITE");
            }
            else {
                System.out.println("Game not found, choose another to observe");
            }
        }catch(NumberFormatException e){
            System.out.println("Invalid game number, please enter a number");
        }
    }
    else {
        System.out.println("Please provide gameName after keyword observe");
    }
}

public static void createGame(String[] splitResult, ServerFacade server) throws BadRequestException {
    if (splitResult.length == 2) {
        try {
            CreateResult createResult = server.create(splitResult[1]);
            System.out.println("Game created");
        } catch (BadRequestException | AlreadyTakenException | UnauthorizedException e) {
            throw new BadRequestException("Could not create game");
        }
    }
    else {
        throw new BadRequestException("Please provide gameName to create");
    }
}

public static void joinGame(String[] splitResult, ServerFacade server, HashMap<Integer, GameData> gamesMap,
                            boolean inGame) throws AlreadyTakenException {
    try {
        if (inGame){
            System.out.println("You are already playing, please first finish your game to join another");
            return;
        }
        if (splitResult.length == 3) {
            int gameNumber = Integer.parseInt(splitResult[1]);
            if (!splitResult[2].equals("WHITE") && !splitResult[2].equals("BLACK")) {
                throw new BadRequestException("Please enter either WHITE or BLACK as color");
            }
            //goes into the map and gets the gameID from the listed number
            if (gamesMap.containsKey(gameNumber)) {
                int gameID = gamesMap.get(gameNumber).gameID();
                server.join(new JoinRequest(splitResult[2], gameID));
                System.out.println("Game joined as " + splitResult[2]);

                //get and print out ChessBoard
                ChessGame game = gamesMap.get(gameNumber).game();
                ChessBoardUI chessBoard = new ChessBoardUI(game.getBoard());
                chessBoard.run(splitResult[2]);
                inGame = true;
            } else {
                System.out.println("game not found, please try again");
            }
        }
        else{
            System.out.println("Please provide valid player color and game ID to join");
        }
    }catch (NumberFormatException e) {
        System.out.println("Invalid game number: must be an integer.");
    } catch (UnauthorizedException e) {
        System.out.println("You are not authorized to join this game.");
    } catch (BadRequestException e) {
        System.out.println("Bad request: " + e.getMessage());
    }
}

public static void listGames(ServerFacade server, HashMap<Integer, GameData> gamesMap)
        throws BadRequestException, AlreadyTakenException {
    try{
    ListResult listResult = server.list();
    HashSet<GameData> games = listResult.games();
    if (!games.isEmpty()){
        int counter = 1;
        for (GameData game : games) {
            System.out.printf("%d: %s, whiteUsername = %s, blackUsername = %s\n",
                    counter, game.gameName(), game.whiteUsername(), game.blackUsername());
            gamesMap.put(counter, game);
            counter += 1;
        }
    }
    else{System.out.println("No games to list. Please create game to add to database");}

} catch (UnauthorizedException e) {
        System.out.println("Unauthorized: You need to log in first.");
    } catch (BadRequestException e) {
        throw new BadRequestException(e.getMessage());
    } catch (AlreadyTakenException e) {
        throw new AlreadyTakenException(e.getMessage());
    }
}

}
