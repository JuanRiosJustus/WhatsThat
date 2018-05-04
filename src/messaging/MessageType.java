package messaging;

public enum MessageType {
	
	Unknown("Unknown"),
	Chat("Chat"),
	Connect("Connect"),
	Disconnect("Disconnect"),
	Done("Done"),
	GameOn("GameOn"),
	JustConnected("~NULL~Connect"),
	JustDisconnected("~NULL~Disconnect"),
	ServerActionsCompleted("Server~NULL~Done");
	
	private final String message;
	
	MessageType(String msg) { message = msg; }
	
	public static boolean isChatMessage(String msg) { return msg.equalsIgnoreCase(Chat.toString()); }
	public static boolean isConnectMessage(String msg) { return msg.equalsIgnoreCase(Connect.toString()); }
	public static boolean isDisconnectMessage(String msg) { return msg.equalsIgnoreCase(Disconnect.toString()); }
	public static boolean isDoneMessage(String msg) { return msg.equalsIgnoreCase(Done.toString()); }
	
	public static MessageType getMessageType(String msgType) {
		if (isChatMessage(msgType)) {
			return Chat;
		} else if (isConnectMessage(msgType)) {
			return Connect;
		} else if (isDisconnectMessage(msgType)) {
			return Disconnect;
		} else if (isDoneMessage(msgType)) {
			return Done;
		} else {
			return Unknown;
		}
	}
	
	public static String constructServerMesaage(String msg) { return "Server~" + msg + "~Chat"; }
	
	public String toString() { return message; }
}
