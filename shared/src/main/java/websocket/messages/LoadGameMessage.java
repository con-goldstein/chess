package websocket.messages;
import com.google.gson.Gson;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    ChessGame game;
    public LoadGameMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
