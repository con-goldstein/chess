import websocket.WebSocketFacade;
import server.ServerFacade;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import requests.*;

import java.io.IOException;

public class PreRepl {
    ServerFacade server;
    PostRepl postRepl;
    Repl repl;
    String username;

    public PreRepl(ServerFacade serverFacade, Repl repl){
         this.server = serverFacade;
         this.repl = repl;
         postRepl = new PostRepl(server, this, repl);
    }
    public void eval(String result) throws BadRequestException, AlreadyTakenException,
            UnauthorizedException, DataAccessException, IOException {
        var words = result.split(" ");
        String keyWord = words[0];
        switch (keyWord){
            case "register":
                if (words.length == 4) {
                    // "register", "username", "password", "email"
                    username = words[1];
                    server.register(new RegisterRequest(words[1], words[2], words[3]));
                    repl.loggedIn = true;
                    System.out.println("you are now registered and logged in as " + words[1]);
                   try {
                   }
                   catch (Exception e){
                       System.out.println("error");
                   }
                    postRepl.postEval(username);
                    break;
                }
                else {
                    throw new BadRequestException("Provide valid username, password and email");
                    }
            case "login":
                if (words.length == 3){
                    username = words[1];
                    server.login(new LoginRequest(words[1], words[2]));
                    repl.loggedIn = true;
                    System.out.println("You are now logged in as " + words[1]);
                    postRepl.postEval(username);
                    break;
                }
                else{
                    throw new BadRequestException("Provide valid username and password");
                }
            default:
                throw new BadRequestException("Provide valid keyword please (register, login, quit, help)");
        }
    }
}
