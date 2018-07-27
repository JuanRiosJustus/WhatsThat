package responsesRequests;

import messaging.MessageType;
import messaging.Request;

public class GameStartRequest extends Request {

    private String host;
    private String topic;
    private String[] players;
    private int rounds;

    public GameStartRequest(String host, String topic, String[] players, int rounds) {
        super(host, topic, MessageType.Command.getValueAsInt());
        this.host = host;
        this.topic = topic;
        this.players = players;
        this.rounds = rounds;
    }

    public String getHost() { return host; }
    public String[] getPlayers() { return players; }
    public String getTopic() { return topic; }
    public int getRounds() { return rounds; }
}
