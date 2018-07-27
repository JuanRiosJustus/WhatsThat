package commands;

import javafx.scene.control.TextField;
import messaging.*;
import network.IOQueue;
import network.User;
import responsesRequests.GameStartRequest;
import responsesRequests.GameStartResponse;
import utlility.GameCommandUtils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandler {

    private boolean gameIsStarted;

    /**
     * TODO
     * Given a command, handles the command appropriately, returning a response back
     * with information regarding the success of the given game state with regards
     * to the given command.
     * @param msg the message that contains the command
     * @return a string representing the current game handler's state for the given input
     */
    public Message handleCommand(ConcurrentHashMap<String, User> map, IOQueue queue, Message msg, boolean isHost) {
        CommandType type = getCommandType(msg);
        System.out.println( "Command entered: " + type.toString() );
        if (CommandType.StartGame == type) {
            return handleStartGameCommand(map, queue, msg);
        }
        return MessageUtils.constructMessage("SERVER", "COMMAND UNSUPPORTED...", MessageType.Public);
    }

    // ensure this returns a response
    private GameStartResponse handleStartGameCommand(ConcurrentHashMap<String, User> map, IOQueue queue, Message msg) {
        //  STRING_COMMAND_TOKEN(0) STRING_COMMAND_NAME(1) STRING_TOPIC(2) INT_ROUNDS(3)
        GameStartResponse response = GameCommandUtils.isCorrectlyFormattedGameStartCommand(msg);
        if (!response.getSuccess()) {
            return response;
        }
        GameStartRequest gsr = new GameStartRequest(msg.getAuthor(), getTopic(msg), getPlayers(map), getRounds(msg));
        // send game the info and list of users.
        queue.enqueueFromExternal(gsr);
        gameIsStarted = true;
        return response;
    }

    /**
     * Gets the particular command type of the given string based on its contents.
     * @param msg message to interperet
     * @return the type of command given.
     */
    private CommandType getCommandType(Message msg) {
        String cmdName = msg.getContent().split("\\s+")[1];
        CommandType type = CommandType.Unsupported;
        for (CommandType cmd : CommandType.values()) {
            if (cmd.toString().equalsIgnoreCase(cmdName)) {
                type = cmd;
            }
        }
        return type;
    }

    /**
     * Retrieves the ist of users and puts them into an array.
     * @param map the map of users.
     * @return the names of all the users.
     */
    private String[] getPlayers(ConcurrentHashMap<String, User> map){
        List<User> mapers = new ArrayList<User>(map.values());
        String[] players = new String[mapers.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = mapers.get(i).getName();
        }
        return players;
    }

    public boolean hasGameStarted() { return gameIsStarted; }
    private int getRounds(Message msg) {
        return Integer.valueOf(msg.getContent().split("\\s+")[3]);
    }
    private String getTopic(Message msg) {
        return msg.getContent().split("\\s+")[2];
    }
}
