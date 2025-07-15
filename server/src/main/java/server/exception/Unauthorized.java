package server.exception;

import spark.*;

public class Unauthorized {
    public static Object response(Response res){
        res.status(401);
        return "{ \"message\": \"Error: unauthorized\"}";
    }
}
