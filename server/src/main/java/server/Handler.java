package server;
import dataAccess.*;
import model.*;
import server.exception.*;
import service.*;
import Requests.*;
import Results.*;
import com.google.gson.Gson;
import spark.*;

import java.util.HashSet;

public class Handler {
    public UserDAO userDAO = new MemoryUserDAO();
    public AuthDAO authDAO = new MemoryAuthDAO();
    public GameDAO gameDAO = new MemoryGameDAO();


    public Object login(Request req, Response res) {
        res.type("application/json");
        var serializer = new Gson();
        var json = serializer.fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult loginResult = UserService.login(json, userDAO, authDAO);
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
            RegisterResult registerResult = UserService.register(json, userDAO, authDAO);
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

    public Object logout(Request req, Response res){
        String authToken = req.headers("authorization");
        try{
            //returns "{}"
            UserService.logout(new LogoutRequest(authToken), authDAO);
            System.out.println("response status: " + res.status());
            return "{}";
        }
        catch (UnauthorizedException e) {
            res.status(401);
            return "{ \"message\": \"Error:\" }";
        }
    }

    public Object listGames(Request req, Response res){
        String authToken = req.headers("authorization");
        try{
            HashSet<GameData> listResult = GameService.list(authToken, authDAO, gameDAO);
            for (GameData game : listResult){
                System.out.println(game);
            }
            return new Gson().toJson(new ListResult(listResult));
        } catch (UnauthorizedException e) {
            return Unauthorized.response(res);
        }
    }

    public Object createGame(Request req, Response res){
        try{
            String authToken = req.headers("authorization");
            GameRequest gameRequest = new Gson().fromJson(req.body(), GameRequest.class);
            CreateResult createResult = GameService.create(new CreateRequest(authToken, gameRequest.gameName())
                    ,authDAO, gameDAO);
            return new Gson().toJson(createResult);
        } catch(UnauthorizedException e){
            return Unauthorized.response(res);
        } catch(BadRequestException e){
            return BadRequest.response(res);
        } catch(Exception e){
            res.status(500);
            return "{ \"message\": \"Error:\" }";
        }
    }

    public Object joinGame(Request req, Response res){
        try{
            String authToken = req.headers("authorization");
            JoinRequest joinRequest = new Gson().fromJson(req.body(), JoinRequest.class);
            GameService.join(authToken, joinRequest, authDAO, gameDAO);
            return "{}";
        }
        catch(AlreadyTakenException e){
            return AlreadyTaken.response(res);
        }
        catch(BadRequestException e){
            return BadRequest.response(res);
        }catch(UnauthorizedException e){
            return Unauthorized.response(res);
        }
        catch(Exception e){
            res.status(500);
            return "{ \"message\": \"Error:\" }";
        }
    }
    public Object clear(Request req, Response res){
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            return "{}";
        }
        catch(Exception e){
            res.status(500);
            return "{ \"message\": \"Error:\" }";
        }
    }

    public record GameRequest(String gameName){}
}

