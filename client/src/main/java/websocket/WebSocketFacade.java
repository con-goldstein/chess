package websocket;
import chess.ChessMove;
import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import websocket.ServerMessageObserver;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.imageio.spi.ServiceRegistry;
import javax.websocket.*;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class WebSocketFacade extends Endpoint {
    Session session;
    ServerMessageObserver observer;
    public WebSocketFacade(ServerMessageObserver observer) throws Exception {
        this.observer = observer;

        URI socketURI = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);
        //set message handler
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String s) {
                observer.notify(s);
            }
        });
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }


    public void connectToServer(String authToken, int gameID) throws IOException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        MakeMoveCommand userGameCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
    }

    public void leave(String authToken, int gameID) throws IOException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        this.session.close();
    }

    public void resign(String authToken, int gameID) throws IOException {
        UserGameCommand userGameCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
    }
}
