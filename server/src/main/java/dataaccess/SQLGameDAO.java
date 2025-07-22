package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import results.CreateResult;
import java.util.Random;
import java.sql.SQLException;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException, SQLException{
        DatabaseManager.createDatabase();
        DatabaseManager.createGameDatabase();
    }
    public void clear(){
        try (var conn = DatabaseManager.getConnection()){
            var statement = "TRUNCATE TABLE game";
            var prepstatement = conn.prepareStatement(statement);
            prepstatement.executeUpdate();
        }
        catch (SQLException | DataAccessException e){}
    }

    public HashSet<GameData> findGames(){
         HashSet<GameData> hashset = new HashSet<>();
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM game";
            try(var preparedStatement = conn.prepareStatement(statement)){
                try(var rs = preparedStatement.executeQuery()){
                    while (rs.next()){
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var json = rs.getString("ChessGame");
                        var chessGame = new Gson().fromJson(json, ChessGame.class);
                        hashset.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                    return hashset;
                }
            }
        }
        catch (SQLException | DataAccessException e){
            System.out.println("could not find Games correctly");
            return null;
        }
    }
    public CreateResult createGameData(String gamename) throws BadRequestException{
        Random random = new Random();
        int gameID = random.nextInt(1000);
        try (var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, ChessGame)" +
                    "VALUES(?, ?, ?, ?, ?)";
            try(var preparedStatement = conn.prepareStatement(statement)) {
                ChessGame chessGame = new ChessGame();
                String json = new Gson().toJson(chessGame);
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, "white");
                preparedStatement.setString(3, "black");
                preparedStatement.setString(4, gamename);
                preparedStatement.setString(5, json);
                preparedStatement.executeUpdate();
                return new CreateResult(gameID);
            }
        } catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
            throw new BadRequestException("could not create game");
        }
    }


    public void addGame(GameData game){
        int gameID = game.gameID();
        try (var conn = DatabaseManager.getConnection()){
            var statement = "DELETE FROM game WHERE gameID=?";
            try(var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.executeUpdate();
                try(var preparedStatement2 = conn.prepareStatement(
                        "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, ChessGame)" +
                                "VALUES(?, ?, ?, ?, ?)")) {
                    preparedStatement2.setInt(1, gameID);
                    preparedStatement2.setString(2, game.whiteUsername());
                    preparedStatement2.setString(3, game.blackUsername());
                    preparedStatement2.setString(4, game.gameName());
                    var json = new Gson().toJson(game.game());
                    preparedStatement2.setString(5, json);
                    preparedStatement2.executeUpdate();
                }
            }
        }
        catch(SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
