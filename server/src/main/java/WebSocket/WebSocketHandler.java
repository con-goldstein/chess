package WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import dataaccess.*;
import exceptions.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.Server;
import service.GameService;
import websocket.commands.*;
import com.google.gson.Gson;
import websocket.messages.*;
import websocket.messages.ServerMessage;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.Collection;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    AuthDAO authDAO;
    GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        }


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws DataAccessException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            int gameID = command.getGameID();
            String username;
            try {
                username = getUsername(command.getAuthToken());
            }
            catch (DataAccessException e){
                ErrorMessage errmsg = new ErrorMessage("Error: Invalid authToken or gameDAO");
                Connection errorConnection = new Connection("could not find user", session);
                errorConnection.send(errmsg.toString());
                return;
            }
            GameData game = GameService.findGame(gameID, gameDAO);
            if (game == null){
                //send error message to client
                ErrorMessage errmsg = new ErrorMessage("Error: Invalid authToken or gameDAO");
                Connection errorConnection = new Connection(username, session);
                errorConnection.send(errmsg.toString());
                return;
            }
            //saveSession
            connections.saveSession(gameID, new Connection(username, session));

            switch (command.getCommandType()){
                case CONNECT -> connect(username, gameID, game, command);
                case MAKE_MOVE -> makeMove(msg, gameID, username);
                case LEAVE -> leaveGame();
                case RESIGN -> resign();
                default -> System.out.println("for show until functions are made");
            }
        } catch (DataAccessException | IOException | InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void connect(String username, int gameID, GameData game, UserGameCommand command)
            throws IOException, DataAccessException {
        //server sends LOAD_GAME message back to root client
        //server sends NOTIFICATION message to everybody else

        String authToken = command.getAuthToken();

        //authenticate authToken & gameID
        try{
            authDAO.getauthData(authToken);
            GameService.findGame(gameID, gameDAO);
        } catch (DataAccessException e){
            ErrorMessage errmsg = new ErrorMessage("error: Invalid authToken or gameDAO");
            connections.broadcastToUser(errmsg.toString(), gameID, username);
            connections.broadcastNotToUser(errmsg.toString(), gameID, username);
        }

        //if username == whiteUsername, user joined as white
        String string = "";
        if (game.whiteUsername().equals(username)){
            string += "WHITE";
        }
        else if (game.blackUsername().equals(username)){
            string += "BLACK";
        }
        //if none of them, user joined as an observer
        else {
            string += "observer";
        }

        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game());
        connections.broadcastToUser(loadGameMessage.toString(), gameID, username);

        var message = String.format("%s has joined the game as %s", username, string);
        NotificationMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastNotToUser(notificationMessage.toString(), gameID, username);
    }

    public void makeMove(String msg, int gameID, String username) throws DataAccessException, IOException, InvalidMoveException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(msg, MakeMoveCommand.class);
        //check valid moves
        ChessMove move = makeMoveCommand.getMove();
        ChessPosition endPos = move.getEndPosition();
        ChessGame game = GameService.findGame(makeMoveCommand.getGameID(), gameDAO).game();
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
        boolean isValid = validMoves.stream().anyMatch(validMove -> validMove.getEndPosition().equals(endPos));
        if (!isValid){
            ErrorMessage errmsg = new ErrorMessage("error: Invalid authToken or gameDAO");
            connections.broadcastToUser(errmsg.toString(), gameID, username);
            connections.broadcastNotToUser(errmsg.toString(), gameID, username);
        }
        else{
            //update the board (make the move)
            game.makeMove(move);
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            //send LOAD_GAME message to all
            connections.broadcastToAll(loadGameMessage.toString(), gameID);

            var message = String.format("%s made a move from %s to %s",
                    username, move.getStartPosition().toString(), move.getEndPosition().toString());
            NotificationMessage notificationMessage = new NotificationMessage(message);
            //send Notification to all but root client
            connections.broadcastNotToUser(notificationMessage.toString(), gameID, username);

            String checkMsg = "";
            if (game.isInCheck(game.getTeamTurn())){
                checkMsg += "Player is in check";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }
            if (game.isInCheckmate(game.getTeamTurn())){
                checkMsg += "Player is in checkmate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }
            if (game.isInStalemate(game.getTeamTurn())){
                checkMsg += "Player is in stalemate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }

        }

    }
    public void leaveGame(){}
    public void resign(){}


    public String getUsername(String authToken) throws DataAccessException, IOException {
        AuthData authData = authDAO.getauthData(authToken);
        if (authData == null){
            throw new DataAccessException("error: invalid authToken");
        }
        return authData.username();
    }
}
