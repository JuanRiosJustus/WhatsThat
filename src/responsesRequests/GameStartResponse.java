package responsesRequests;

import messaging.Message;
import messaging.MessageType;

import java.util.Arrays;

public class GameStartResponse extends Message {
    // DETAILS ABOUT THE GAME STARTING

    private String[] players;
    private boolean wasSuccessful;

    public GameStartResponse(String host, String content, String[] players, boolean success) {
        super(host, content, MessageType.Command.getValueAsInt());
        this.players = players;
        wasSuccessful = success;
    }
    public String[] getPlayers() {
        return players;
    }
    public String getTopic() {
        return getContent();
    }
    public boolean getSuccess() {
        return wasSuccessful;
    }

}
