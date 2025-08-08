package websocket.messages;
import com.google.gson.Gson;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage{
    GameData game;
    public LoadGameMessage(GameData game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public String toString() {
        return new Gson().toJson(this);
    }

    public GameData getGame(){
        return game;
    }
}
