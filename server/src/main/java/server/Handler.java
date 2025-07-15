package server;
import dataaccess.*;
import server.exception.*;
import service.*;
import Requests.*;
import Results.*;
import com.google.gson.Gson;
import spark.*;

public class Handler {
    public UserDAO userDAO = new MemoryUserDAO();
    public AuthDAO authDAO = new MemoryAuthDAO();
    public GameDAO gameDAO = new MemoryGameDAO();


    public Object login(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        var json = serializer.fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult loginResult = UserService.login(json, res, userDAO);
            return serializer.toJson(loginResult);
        } catch (BadRequestException e) {
            return BadRequest.response(res);
        } catch (UnauthorizedException e) {
            return Unauthorized.response(res);
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error:\" }";
        }
    }

    public Object register(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        var json = serializer.fromJson(req.body(), RegisterRequest.class);
        try {
            //register user
            RegisterResult registerResult = UserService.register(json, res, userDAO);
            //return registerResult in json format
            return serializer.toJson(registerResult);
        } catch (AlreadyTakenException e) {
            return AlreadyTaken.response(res);
        } catch (BadRequestException e) {
            return BadRequest.response(res);
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error:\" }";
        }
    }

//    public Object logout(Request req, Response res){
//        res.type("applicaion/json")
//    }

//    public void clear(Request req, Response res){
//        userDAO.clear();
//        authDAO.clear();
//        gameDAO.clear();
//    }
}

