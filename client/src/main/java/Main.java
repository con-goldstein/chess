import dataaccess.Repl;

public class Main {
    public static void main(String[] args) {
        // connect with repl in here
//        Server server = new Server();
        Repl repl = new Repl();
        repl.run();
    }
}