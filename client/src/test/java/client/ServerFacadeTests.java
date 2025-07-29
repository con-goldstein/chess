package client;

import dataaccess.*;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;
import model.GameData;
import org.junit.jupiter.api.*;
import requests.*;
import results.*;
import server.Server;
import Server.ServerFacade;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

   @BeforeEach
   public void beforeEach() throws SQLException, DataAccessException {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
   }



    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void goodRegisterTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult result = facade.register(new RegisterRequest("username", "password", "email"));
        assertEquals("username", result.username());
        System.out.println(result.authToken());
        assertTrue(result.authToken().length() > 10);
    }

    @Test public void badRegisterTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException{
        facade.register(new RegisterRequest("username", "password", "email"));
        assertThrows(AlreadyTakenException.class, () -> {
            facade.register(new RegisterRequest("username", "password", "email"));
        });
    }

    @Test public void goodLoginTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        facade.register(new RegisterRequest("username","password", "email"));
        LoginResult result = facade.login(new LoginRequest("username", "password"));
        assertEquals("username", result.username());
        assertTrue(result.authToken().length() > 10);
    }

    @Test public void badLoginTest() {
        assertThrows(UnauthorizedException.class, () -> {
            facade.login(new LoginRequest("username", "password"));
        });
    }

    @Test public void goodCreateTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        facade.create("gameName");
        HashSet<GameData> games = gameDAO.findGames();
        assertEquals(1, games.size());
    }

    @Test public void goodCreateTest2() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        facade.create("gameName");
        facade.create("anotherGameName");
        HashSet<GameData> games = gameDAO.findGames();
        assertEquals(2, games.size());
    }

    @Test public void badCreateTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        facade.create("gameName");
        assertThrows(BadRequestException.class, () -> {
            facade.create("gameName");
        });
    }

    @Test public void goodListTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        facade.create("gameName");
        ListResult result = facade.list();
        HashSet<GameData> games = result.games();
        assertEquals(1, games.size());
    }

    @Test public void goodListTest2() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        facade.create("gameName");
        facade.create("anotherGameName");
        ListResult result = facade.list();
        HashSet<GameData> games = result.games();
        assertEquals(2, games.size());
    }

    @Test public void badListTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        assertThrows(UnauthorizedException.class, () -> {
            facade.list();
        });
    }

    @Test public void goodLogoutTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException, DataAccessException {
        facade.register(new RegisterRequest("username","password", "email"));
        facade.login(new LoginRequest("username", "password"));
        assertTrue(facade.logout());
        }


        @Test public void badLogoutTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        assertThrows(UnauthorizedException.class, () -> {
            facade.logout();
        });
        }

        @Test public void goodJoinTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
            facade.register(new RegisterRequest("username","password", "email"));
            facade.login(new LoginRequest("username", "password"));
            CreateResult result = facade.create("gameName");
            facade.join(new JoinRequest("WHITE", result.gameID()));
            ListResult listResult = facade.list();
            HashSet<GameData> games = listResult.games();
            for (GameData game : games){
                assertEquals("username", game.whiteUsername());
            }
        }

        @Test public void badJoinTest() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
            facade.register(new RegisterRequest("username","password", "email"));
            facade.login(new LoginRequest("username", "password"));
            CreateResult result = facade.create("gameName");
            assertThrows(BadRequestException.class, () -> {facade.join(new JoinRequest("color", result.gameID()));});
        }
}
