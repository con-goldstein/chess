package websocket.messages;
import com.google.gson.Gson;

public class ErrorMessage extends ServerMessage{
    String errorMessage;
    public ErrorMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
