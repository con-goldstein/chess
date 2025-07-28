import Server.ServerFacade;
import dataaccess.*;
import requests.*;
import server.exception.AlreadyTaken;

public class preRepl {
    ServerFacade server;
    postRepl postRepl;
    repl repl;
    public preRepl(ServerFacade serverFacade, repl repl){
         this.server = serverFacade;
         postRepl = new postRepl(server, this);
         this.repl = repl;
}
    public void eval(String result) throws BadRequestException, AlreadyTakenException,
            UnauthorizedException {
        var words = result.split(" ");
        String keyWord = words[0];
        switch (keyWord){
            case "register":
                if (words.length == 4) {
                    // "register", "username", "password", "email"
                    server.register(new RegisterRequest(words[1], words[2], words[3]));
                    System.out.println("you are now registered and logged in as " + words[1]);
                    postRepl.postEval();
                }
                else {
                    throw new BadRequestException("Provide valid username, password and email");
                    }
            case "login":
                if (words.length == 3){
                    server.login(new LoginRequest(words[1], words[2]));
                    System.out.println("You are now logged in as " + words[1]);
                    postRepl.postEval();
                }
                else{
                    throw new BadRequestException("Provide valid username and password");
                }
        }
        throw new BadRequestException("Provide valid keyword please (register, login, quit, help");
    }
}
