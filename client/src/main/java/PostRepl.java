import Server.ServerFacade;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.GameData;
import requests.*;
import results.*;

import java.util.*;

import static ui.EscapeSequences.RESET_TEXT_BOLD_FAINT;
import static ui.EscapeSequences.SET_TEXT_FAINT;

public class PostRepl {
ServerFacade server;
PreRepl preRepl;
HashMap<Integer, GameData> gamesMap = new HashMap<>();
Repl repl;

public PostRepl(ServerFacade serverFacade, PreRepl preRepl, Repl repl){
    this.server = serverFacade;
    this.preRepl = preRepl;
    this.repl = repl;
}

public void postEval() throws BadRequestException, AlreadyTakenException,
        UnauthorizedException, DataAccessException {
    String helpLine = "create <Name> - a game\n" +
            "list - game\n join <ID> [WHITE/BLACK] - a game\n observe <ID> - a game\n logout- when you are done\n" +
            "quit - playing chess\n help - with possible commands";
    System.out.println(helpLine);
    Scanner scanner = repl.scanner;
    while (true) {
        System.out.print(SET_TEXT_FAINT + "Input action >>> " + RESET_TEXT_BOLD_FAINT);
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
                        System.out.println("logged out successfully\n");
                        repl.loggedIn = false;
                        break;
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
                            break;
                        }
                        else{System.out.println("No games to list. Please create game to add to database"); break;}

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                case "join":
                    if (splitResult.length == 3) {
                        int gameNumber = Integer.parseInt(splitResult[1]);
                        //goes into the map and gets the gameID from the listed number
                        if (gamesMap.containsKey(gameNumber)) {
                            int gameID = gamesMap.get(gameNumber).gameID();
                            server.join(new JoinRequest(splitResult[2], gameID));
                            System.out.println("Game joined");
                            break;
                        } else {
                            System.out.println("game not found, please try again");
                            break;
                        }
                    } else {
                        System.out.println("Please provide valid player color and game ID to join");
                    }
                case "observe":
                    if (splitResult.length == 2) {
                        int gameNumber = Integer.parseInt(splitResult[1]);
                        if (gamesMap.containsKey(gameNumber)) {
                            int gameID = gamesMap.get(gameNumber).gameID();
                            System.out.println("game is observable with gameID " + gameID);
                            //call observe function?
                            break;
                        }
                    }
            }
    }
}

}
