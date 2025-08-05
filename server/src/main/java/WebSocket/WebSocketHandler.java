package WebSocket;

import chess.*;
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
import java.util.Objects;

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
            if (game.game().isInCheckmate(game.game().getTeamTurn())){
                //send error message to user only
                ErrorMessage errorMsg = new ErrorMessage("You are in checkmate, cannot make valid move");
                connections.broadcastToUser(errorMsg.toString(), gameID, username);
            }
            else{
                //saveSession
                connections.saveSession(gameID, new Connection(username, session));
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
                switch (command.getCommandType()){
                    case CONNECT -> connect(username, gameID, game);
                    case MAKE_MOVE -> makeMove(msg, gameID, username);
                    case LEAVE -> leaveGame();
                    case RESIGN -> resign();
                    default -> System.out.println("for show until functions are made");
                }
            }
        } catch (DataAccessException | IOException | InvalidMoveException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void connect(String username, int gameID, GameData game)
            throws IOException, DataAccessException {
        //server sends LOAD_GAME message back to root client
        //server sends NOTIFICATION message to everybody else

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
        GameData gameData = GameService.findGame(makeMoveCommand.getGameID(), gameDAO);
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());

        ChessGame.TeamColor teamColor = game.getTeamTurn();
        ChessGame.TeamColor pieceColor = board.getPiece(move.getStartPosition()).getTeamColor();

        //check if correct user is making move
        if (teamColor.equals(ChessGame.TeamColor.WHITE)){
            if (!Objects.equals(username, gameData.whiteUsername())){
                ErrorMessage errmsg = new ErrorMessage("error: Only white user can make a move");
                connections.broadcastToUser(errmsg.toString(), gameID, username);
            }
            else{
                    if (!teamColor.equals(pieceColor)){
                        ErrorMessage errmsg = new ErrorMessage("error: Made a move while it was not your turn");
                        connections.broadcastToUser(errmsg.toString(), gameID, username);
                    }
                    else {
                        finishMakeMove(endPos, validMoves, gameID, username, gameData, move);
                        game.setTeamTurn(ChessGame.TeamColor.BLACK);
                    }
                }
        }
        else if (teamColor.equals(ChessGame.TeamColor.BLACK)){
            if (!Objects.equals(username, gameData.blackUsername())){
                ErrorMessage errmsg = new ErrorMessage("error: Only black user can make a move");
                connections.broadcastToUser(errmsg.toString(), gameID, username);
            }
            else{
                if (!teamColor.equals(pieceColor)){
                    ErrorMessage errmsg = new ErrorMessage("error: Made a move while it was not your turn");
                    connections.broadcastToUser(errmsg.toString(), gameID, username);
                }
                else {
                    finishMakeMove(endPos, validMoves, gameID, username, gameData, move);
                    game.setTeamTurn(ChessGame.TeamColor.WHITE);
                }
            }
        }


    }

    public void finishMakeMove(ChessPosition endPos, Collection<ChessMove> validMoves, int gameID, String username, GameData gameData,
                   ChessMove move) throws IOException, InvalidMoveException, DataAccessException {
        ChessGame game = gameData.game();
        boolean isValid = isMoveValid(endPos, validMoves);
        if (!isValid) {
            ErrorMessage errmsg = new ErrorMessage("error: Invalid authToken or gameDAO");
            connections.broadcastToUser(errmsg.toString(), gameID, username);
        } else {
            //update the board (make the move)
            game.makeMove(move);
            gameDAO.addGame(gameData);
            LoadGameMessage loadGameMessage = new LoadGameMessage(game);
            //send LOAD_GAME message to all
            connections.broadcastToAll(loadGameMessage.toString(), gameID);

            var message = String.format("%s made a move from %s to %s",
                    username, move.getStartPosition().toString(), move.getEndPosition().toString());
            NotificationMessage notificationMessage = new NotificationMessage(message);
            //send Notification to all but root client
            connections.broadcastNotToUser(notificationMessage.toString(), gameID, username);

            String checkMsg = "";
            if (game.isInCheck(game.getTeamTurn()) & !game.isInCheckmate(game.getTeamTurn())) {
                checkMsg += "Player is in check";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }
            if (game.isInCheckmate(game.getTeamTurn())) {
                checkMsg += "Player is in checkmate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }
            if (game.isInStalemate(game.getTeamTurn())) {
                checkMsg += "Player is in stalemate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }

        }
    }

    private boolean isMoveValid(ChessPosition endPos, Collection<ChessMove> validMoves) {
        for (ChessMove move : validMoves){
            ChessPosition endPosition = move.getEndPosition();
            if (endPos.equals(endPosition)){
                return true;
            }
        }
        return false;
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
