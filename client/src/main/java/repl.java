import Server.ServerFacade;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class repl {
    preRepl preRepl;
    ServerFacade server;
    public repl(){
        this.server = new ServerFacade("http://localhost:8080");
        this.preRepl = new preRepl(server, this);
    }

public void run() {
    System.out.println("\\uD83D\\uDC36 ♕ Welcome to 240 chess. Type Help to get started. ♕" );
    String helpLine = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account \n" +
            "login <USERNAME> <PASSWORD> - to play chess \n" +
                    "quit - playing chess \n" +
                    "help - with possible commands";
    System.out.println(helpLine);
    Scanner scanner = new Scanner(System.in);
    System.out.print(SET_TEXT_FAINT + "Input action >>> " + RESET_TEXT_BOLD_FAINT);
    String result = scanner.nextLine();
    while (!result.equals("quit")){
        if (result.equals("help")){
            System.out.println(helpLine);
            System.out.print(SET_TEXT_BLINKING + "Input action >>> " + RESET_TEXT_BOLD_FAINT);
            result = scanner.nextLine();
            continue;
        }
        try {
            preRepl.eval(result);
        } catch (AlreadyTakenException | UnauthorizedException | BadRequestException e) {
            System.out.println(e.getMessage());
        }
        System.out.print(SET_TEXT_FAINT + "Input action >>> " + RESET_TEXT_BOLD_FAINT);
        result = scanner.nextLine();
    }
    System.out.println("Thanks for playing chess");
}


//done
}
