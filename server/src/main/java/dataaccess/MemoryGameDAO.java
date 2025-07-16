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
            return new CreateResult(gameData.GameID());
        }
        catch(Exception e){
            throw new BadRequestException("could not");
        }
    }
}
