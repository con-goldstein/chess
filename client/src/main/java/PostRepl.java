import Server.ServerFacade;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.GameData;
import requests.*;
import results.*;

import java.util.*;

import static ui.EscapeSequences.*;

public class PostRepl {
ServerFacade server;
PreRepl preRepl;
HashMap<Integer, GameData> gamesMap = new HashMap<>();
Repl repl;
public boolean inGame = false;

public PostRepl(ServerFacade serverFacade, PreRepl preRepl, Repl repl){
    this.server = serverFacade;
    this.preRepl = preRepl;
    this.repl = repl;
}

public void postEval() throws BadRequestException, AlreadyTakenException,
        UnauthorizedException, DataAccessException {
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
                    if (splitResult.length == 2) {
                        try {
                            CreateResult createResult = server.create(splitResult[1]);
                            System.out.println("Game created with id " + createResult.gameID());
                            break;
                        } catch (BadRequestException e) {
                            throw new BadRequestException("Could not create game");
                        }
                    }
                    else {
                        throw new BadRequestException("Please provide gameName to create");
                    }
                case "list":
                    listGames(server, gamesMap);
                    break;

                case "join":
                    try {
                        if (inGame){
                            System.out.println("You are already playing, please first finish your game to join another");
                            break;
                        }
                        if (splitResult.length == 3) {
                            int gameNumber = Integer.parseInt(splitResult[1]);
                            //goes into the map and gets the gameID from the listed number
                            if (gamesMap.containsKey(gameNumber)) {
                                int gameID = gamesMap.get(gameNumber).gameID();
                                server.join(new JoinRequest(splitResult[2], gameID));
                                System.out.println("Game joined as " + splitResult[2]);
                                inGame = true;
                                break;
                            } else {
                                System.out.println("game not found, please try again");
                            }
                        }
                        else{
                            System.out.println("Please provide valid player color and game ID to join");
                        }
                    }catch (NumberFormatException e){
                        System.out.println("Invalid game number: must be an integer.");
                        }
                    catch (AlreadyTakenException e){throw new AlreadyTakenException("that color is already taken, " +
                            "please choose another color/game");}
                    break;

                case "observe":
                    if (splitResult.length == 2) {
                        try {
                            int gameNumber = Integer.parseInt(splitResult[1]);
                            if (gamesMap.containsKey(gameNumber)) {
                                int gameID = gamesMap.get(gameNumber).gameID();
                                System.out.println("game is observable with gameID " + gameID);
                                //call observe function?
                                break;
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
                    break;
            }
    }
}

public static void listGames(ServerFacade server, HashMap<Integer, GameData> gamesMap){
    try{
    ListResult listResult = server.list();
    HashSet<GameData> games = listResult.games();
    if (!games.isEmpty()){
        int counter = 1;
        for (GameData game : games) {
            System.out.printf("%d: gameName = %s, whiteUsername = %s, blackUsername = %s\n",
                    counter, game.gameName(), game.whiteUsername(), game.blackUsername());
            gamesMap.put(counter, game);
            counter += 1;
        }
    }
    else{System.out.println("No games to list. Please create game to add to database");}

} catch (Exception e) {
    throw new RuntimeException(e);
}}

}
