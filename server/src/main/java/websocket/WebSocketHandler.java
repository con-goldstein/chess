package websocket;

import chess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import dataaccess.*;
import exceptions.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.*;
import service.GameService;
import websocket.commands.*;
import com.google.gson.Gson;
import websocket.messages.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    AuthDAO authDAO;
    GameDAO gameDAO;
    GameData gameData;

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
                ErrorMessage errorMsg = new ErrorMessage("Game is over");
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
                    case RESIGN -> resign(username, gameID, game);
                    case LEAVE -> leaveGame(username, game, gameID, session);
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
        if (username.equals(game.whiteUsername())){
            string += "WHITE";
        }
        else if (username.equals(game.blackUsername())){
            string += "BLACK";
        }
        //if none of them, user joined as an observer
        else {
            string += "observer";
        }

        LoadGameMessage loadGameMessage = new LoadGameMessage(game);
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

        gameData = GameService.findGame(makeMoveCommand.getGameID(), gameDAO);

        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();

        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());

        ChessGame.TeamColor teamColor = game.getTeamTurn();
        ChessGame.TeamColor pieceColor = board.getPiece(move.getStartPosition()).getTeamColor();

        if (game.gameOver()){
            ErrorMessage newErrorMessage = new ErrorMessage("Error: Game is over");
            connections.broadcastToUser(newErrorMessage.toString(), gameID, username);
            return;
        }
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
            ErrorMessage errmsg = new ErrorMessage("error: Invalid move");
            connections.broadcastToUser(errmsg.toString(), gameID, username);
        } else {
            //update the board (make the move)
            game.makeMove(move);
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.addGame(newGameData);
            LoadGameMessage loadGameMessage = new LoadGameMessage(newGameData);
            //send LOAD_GAME message to all
            connections.broadcastToAll(loadGameMessage.toString(), gameID);
            String startCol = getRowLetter(username, move.getStartPosition());
            String endCol = getRowLetter(username, move.getEndPosition());

            var message = String.format("%s made a move from %s%d to %s%d",
                    username, startCol, move.getStartPosition().getRow(), endCol, move.getEndPosition().getRow());
            NotificationMessage notificationMessage = new NotificationMessage(message);
            //send Notification to all but root client
            connections.broadcastNotToUser(notificationMessage.toString(), gameID, username);

            ChessGame.TeamColor teamColor = game.getTeamTurn();
            String checkMsg = "";
            if (game.isInCheck(teamColor) & !game.isInCheckmate(teamColor)) {
                checkMsg += teamColor + " is in check";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
            }
            if (game.isInCheckmate(game.getTeamTurn())) {
                checkMsg += teamColor + " is in checkmate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
                game.setGameOver(true);
            }
            if (game.isInStalemate(game.getTeamTurn())) {
                checkMsg += teamColor + " is in stalemate";
                NotificationMessage notifMessage = new NotificationMessage(checkMsg);
                connections.broadcastToAll(notifMessage.toString(), gameID);
                game.setGameOver(true);
            }

        }
    }

    private String getRowLetter(String username, ChessPosition startPosition) {
        int col = startPosition.getColumn();
        switch (col) {
            case 1 -> {
                return "a";
            }
            case 2 -> {
                return "b";
            }
            case 3 -> {
                return "c";
            }
            case 4 -> {
                return "d";
            }
            case 5 -> {
                return "e";
            }
            case 6 -> {
                return "f";
            }
            case 7 -> {
                return "g";
            }
            case 8 -> {
                return "h";
            }
        }
        return "z";
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

    public void leaveGame(String username, GameData game, int gameID, Session session) throws DataAccessException, IOException {

        if (username.equals(game.whiteUsername())){
            GameData newGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            gameDAO.addGame(newGame);
        }
        else if (username.equals(game.blackUsername())){
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            gameDAO.addGame(newGame);
        }
        connections.remove(gameID, username, session);
        String message = String.format("%s has left the game", username);
        NotificationMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastNotToUser(notificationMessage.toString(), gameID, username);
    }
    public void resign(String username, int gameID, GameData game) throws IOException, DataAccessException {
        if (game.game().gameOver()){
            ErrorMessage newErrorMessage = new ErrorMessage("Error: game is over");
            connections.broadcastToUser(newErrorMessage.toString(), gameID, username);
            return;
        }
        //updating game
        game.game().setGameOver(true);
        if (username.equals(game.whiteUsername())){
            GameData newGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            gameDAO.addGame(newGame);
        }
        else if (username.equals(game.blackUsername())){
            GameData newGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            gameDAO.addGame(newGame);
        }
        else{
            ErrorMessage errorMessage = new ErrorMessage("Error: you are an observer, and do not have the ability to resign");
            connections.broadcastToUser(errorMessage.toString(), gameID, username);
            return;
        }
//        mark game as over
        //send notification message
        String message = String.format("%s has resigned", username);
        NotificationMessage notificationMessage = new NotificationMessage(message);
        connections.broadcastToAll(notificationMessage.toString(), gameID);
    }


    public String getUsername(String authToken) throws DataAccessException, IOException {
        AuthData authData = authDAO.getauthData(authToken);
        if (authData == null){
            throw new DataAccessException("error: invalid authToken");
        }
        return authData.username();
    }
}
