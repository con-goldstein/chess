package server;

import spark.*;

public class Server {
    public Server(){}

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Handler handler = new Handler();
        // Register your endpoints and handle exceptions here.
        Spark.post("/user", handler::register);
        Spark.delete("/db", handler::clear);
        Spark.post("/session", handler::login);
        Spark.delete("/session", handler::logout);
        Spark.get("/game", handler::listGames);
        Spark.post("/game", handler::createGame);
        Spark.put("/game", handler::joinGame);
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
