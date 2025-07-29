package dataaccess;

import Server.ServerFacade;
import requests.*;

public class PreRepl {
    ServerFacade server;
    PostRepl postRepl;
    Repl repl;

    public PreRepl(ServerFacade serverFacade, Repl repl){
         this.server = serverFacade;
         this.repl = repl;
         postRepl = new PostRepl(server, this, repl);
    }
    public void eval(String result) throws BadRequestException, AlreadyTakenException,
            UnauthorizedException, DataAccessException {
        var words = result.split(" ");
        String keyWord = words[0];
        switch (keyWord){
            case "register":
                if (words.length == 4) {
                    // "register", "username", "password", "email"
                    server.register(new RegisterRequest(words[1], words[2], words[3]));
                    repl.loggedIn = true;
                    System.out.println("you are now registered and logged in as " + words[1]);
                    postRepl.postEval();
                    break;
                }
                else {
                    throw new BadRequestException("Provide valid username, password and email");
                    }
            case "login":
                if (words.length == 3){
                    server.login(new LoginRequest(words[1], words[2]));
                    repl.loggedIn = true;
                    System.out.println("You are now logged in as " + words[1]);
                    postRepl.postEval();
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
