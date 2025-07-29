import Server.ServerFacade;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    PreRepl preRepl;
    ServerFacade server;
    public boolean loggedIn;
    public final Scanner scanner = new Scanner(System.in);

    public Repl(){
        this.server = new ServerFacade("http://localhost:8080");
        this.preRepl = new PreRepl(server, this);
        this.loggedIn = false;
    }

public void run() {
    if (!loggedIn) {
        System.out.println("\\uD83D\\uDC36 ♕ Welcome to 240 chess. Type Help to get started. ♕");
    }
    String helpLine = "register <USERNAME> <PASSWORD> <EMAIL> - to create an account \n" +
            "login <USERNAME> <PASSWORD> - to play chess \n" +
                    "quit - playing chess \n" +
                    "help - with possible commands";
    System.out.println(RESET_TEXT_COLOR + helpLine);
    System.out.print(SET_TEXT_COLOR_GREEN + "Input action >>> " + RESET_TEXT_COLOR);
    String result = scanner.nextLine();
    while (!result.equals("quit")){
        if (result.equals("help")){
            System.out.println(helpLine);
            System.out.print(SET_TEXT_COLOR_GREEN + "Input action >>> " + RESET_TEXT_COLOR);
            result = scanner.nextLine();
            continue;
        }
        try {
            preRepl.eval(result);
        } catch (AlreadyTakenException | UnauthorizedException | BadRequestException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        System.out.print(SET_TEXT_COLOR_GREEN + "Input action >>> " + RESET_TEXT_BOLD_FAINT);
        result = scanner.nextLine();
    }
    System.out.println("Thanks for playing chess");
}
//done
}
