package server.exception;

import spark.Response;

public class BadRequest {
    public static Object response(Response res){
        res.status(400);
        return "{ \"message\": \"Error: bad request\" }";
    }
}