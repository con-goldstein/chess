package WebSocket;

import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public interface ServerMessageObserver {
    void notify(ServerMessage serverMessage);
}
