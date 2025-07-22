package dataAccessTests;

import chess.*;
import com.google.gson.Gson;
import dataaccess.*;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import requests.RegisterRequest;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.HashSet;

public class SQLGameDAOTests {
    private GameDAO gameDAO;
    private UserDAO userDAO;
    GameData gameData;

    @BeforeEach
    public void beforeEach() throws SQLException, DataAccessException {
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        gameDAO.clear();
        userDAO.clear();
        userDAO.createUser(new RegisterRequest("username", "password", "email"));
        ChessGame presetGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        presetGame.setBoard(board);
        gameData = new GameData(97, null, null, "gameName", presetGame);
    }

    @Test public void goodCreateGameData() throws DataAccessException{
        try{
            gameDAO.createGameData("gameName");
            try (var conn = DatabaseManager.getConnection()){
                var statement = "SELECT * FROM game WHERE gameName=?";
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setString(1, "gameName");
                try(var res = preparedStatement.executeQuery()) {
                    if (res.next()) {
                        int gameID = 97;
                        String whiteName = res.getString("whiteUsername");
                        String blackName = res.getString("blackUsername");
                        String gameName = res.getString("gameName");
                        String json = res.getString("ChessGame");
                        ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);
                     GameData newdata = new GameData(gameID, whiteName, blackName, gameName, chessGame);
//                     assertEquals(gameData, newdata);
                        assertEquals(gameData.whiteUsername(), whiteName);
                        assertEquals(gameData.blackUsername(), blackName);
                        assertEquals(gameData.gameName(), gameName);
                        assertEquals(gameData.game(), chessGame);
                    }
                }

            }
        } catch (SQLException | DataAccessException | BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test public void badCreateGameData() throws DataAccessException, BadRequestException{
            gameDAO.createGameData("gameName");

            assertThrows(BadRequestException.class, () -> {
                gameDAO.createGameData("gameName");
            });
        }

    @Test public void goodAddGames(){
        gameDAO.addGame(gameData);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM game WHERE gameName=?";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, "gameName");
            try(var res = preparedStatement.executeQuery()) {
                if (res.next()) {
                    int gameID = 97;
                    String whiteName = res.getString("whiteUsername");
                    String blackName = res.getString("blackUsername");
                    String gameName = res.getString("gameName");
                    String json = res.getString("ChessGame");
                    ChessGame chessGame = new Gson().fromJson(json, ChessGame.class);
                    GameData newdata = new GameData(gameID, whiteName, blackName, gameName, chessGame);
                    assertEquals(gameData, newdata);
                }
            }
        }catch (SQLException | DataAccessException e){
            System.out.println("couldn't add game");
        }
    }

    @Test public void goodfindGames() throws BadRequestException{
        gameDAO.createGameData("gameName");
        gameDAO.createGameData("anotherGameName");
        HashSet<GameData> games = gameDAO.findGames();
        boolean gameSize = (games.size() == 2);
        assertTrue(gameSize);
    }

    @Test public void badFindGames() throws BadRequestException{
        gameDAO.createGameData("gameName");
        assertThrows(BadRequestException.class, () -> {
            gameDAO.createGameData("gameName");
        });
    }


    }
