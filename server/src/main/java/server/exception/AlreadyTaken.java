package server.exception;

import spark.*;

public class AlreadyTaken {
    public static Object response(Response res){
        res.status(403);
        return "{ \"message\": \"Error: username already taken\" }";
    }
}
