package service;


import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import requests.*;
import results.CreateResult;
import results.RegisterResult;
import dataaccess.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

class GameServiceTest {
    private static UserDAO user;
    private static AuthDAO auth;
    private static GameDAO game;
    private static RegisterRequest request;
    private static LoginRequest loginRequest;

    @BeforeEach
    public void beforeAll(){
            user = new MemoryUserDAO();
            auth = new MemoryAuthDAO();
            game = new MemoryGameDAO();
            request = new RegisterRequest("username", "password", "email");
            loginRequest = new LoginRequest("username", "password");
        }


    @Test
    //successfuly creates game
    void create() throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException {
        RegisterResult result = UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
        String authToken = result.authToken();
        CreateRequest createRequest = new CreateRequest(authToken, "gameName");
        GameService.create(createRequest, auth, game);
    }

    @Test
    void badCreate1(){
        assertThrows(UnauthorizedException.class, () -> {
        RegisterResult result = UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
        CreateRequest createRequest = new CreateRequest("badToken", "gameName");
        GameService.create(createRequest, auth, game);
        });
    }


    @Test
    //successfuly creates game and lists it
    void goodList()throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException{
        RegisterResult result = UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
        String authToken = result.authToken();
        CreateRequest createRequest = new CreateRequest(authToken, "gameName");
        GameService.create(createRequest, auth, game);
        GameService.list(authToken, auth, game);
    }

    @Test
    void badList() {
        assertThrows(UnauthorizedException.class, () -> {
            RegisterResult result = UserService.register(request, user, auth);
            UserService.login(loginRequest, user, auth);
            String authToken = result.authToken();
            CreateRequest createRequest = new CreateRequest(authToken, "gameName");
            GameService.create(createRequest, auth, game);
            GameService.list("badToken", auth, game);
        });
    }

    @Test
    void goodJoin()throws BadRequestException, UnauthorizedException, AlreadyTakenException,
            DataAccessException{
        RegisterResult result = UserService.register(request, user, auth);
        UserService.login(loginRequest, user, auth);
        String authToken = result.authToken();
        CreateRequest createRequest = new CreateRequest(authToken, "gameName");
        CreateResult res = GameService.create(createRequest, auth, game);
        JoinRequest joinRequest = new JoinRequest("WHITE", res.gameID());
        GameService.join(authToken, joinRequest, auth, game);
    }

    @Test
    void badJoin(){
        assertThrows(BadRequestException.class, () -> {
            RegisterResult result = UserService.register(request, user, auth);
            UserService.login(loginRequest, user, auth);
            String authToken = result.authToken();
            CreateRequest createRequest = new CreateRequest(authToken, "gameName");
            CreateResult res = GameService.create(createRequest, auth, game);
            JoinRequest joinRequest = new JoinRequest("WRONGCOLOR", res.gameID());
            GameService.join(authToken, joinRequest, auth, game);
        });
    }

    @Test
    void badJoin1(){
        //try and join when another player is already the color you want
        assertThrows(BadRequestException.class, () -> {
            RegisterResult result = UserService.register(request, user, auth);
            UserService.login(loginRequest, user, auth);
            String authToken = result.authToken();
            CreateRequest createRequest = new CreateRequest(authToken, "gameName");
            CreateResult res = GameService.create(createRequest, auth, game);
            JoinRequest joinRequest = new JoinRequest("White", res.gameID());
            GameService.join(authToken, joinRequest, auth, game);
            JoinRequest joinRequest2 = new JoinRequest("White", res.gameID());
            GameService.join(authToken, joinRequest2, auth, game);
        });
    }
}
