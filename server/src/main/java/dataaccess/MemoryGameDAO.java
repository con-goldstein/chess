package dataaccess;

import Results.CreateResult;
import chess.ChessGame;
import model.*;
import java.util.Random;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private final HashSet<GameData> gameDatabase = new HashSet<>();

    public void clear() {
        gameDatabase.clear();
    }
    public HashSet<GameData> findGames(){
        return gameDatabase;
    }

    public CreateResult createGameData(String gameName, String authToken) throws BadRequestException{
        Random random = new Random();
        int gameID = random.nextInt(1000);
        try {
            GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
            gameDatabase.add(gameData);
            return new CreateResult(gameData.gameID());
        }
        catch(Exception e){
            throw new BadRequestException("could not");
        }
    }
    public void addGame(GameData game){
        int gameID = game.gameID();
        for (GameData oldGame : gameDatabase){
            if (oldGame.gameID() == gameID){
                gameDatabase.remove(oldGame);
                gameDatabase.add(game);
            }
        }
    }
}
