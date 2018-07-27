package messaging;

public enum MessageType {
	
	Unsupported("Unsupported", 0),
	Public("Public", 1),
	Connect("Connect", 2),
	Disconnect("Disconnect", 3),
	Command("Command", 4),
	Update("Update", 5),
	Game("Game", 6),
	Draw("Draw", 7);
	
	private final String name;
	private final int value;
	
	MessageType(String nme, int val) {
		name = nme;
		value = val;
	}

    public int getValueAsInt() { return value; }
    public String getValueAsString() { return value + ""; }
	public String toString() { return name; }
}
