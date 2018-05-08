package messaging;

public enum MessageType {
	
	Unknown("Unknown"),
	Public("Public"),
	Private("Private"),
	Connect("Connect"),
	Disconnect("Disconnect"),
	Done("Done"),
	Game("Game"),
	JustConnected("~NULL~Connect"),
	JustDisconnected("~NULL~Disconnect"),
	ServerActionsCompleted("Server~NULL~Done");
	
	private final String message;
	
	MessageType(String msg) { message = msg; }
	
	/**
	 * Given a string determines if the given string matches a particular
	 * constant's string representation.
	 * @param msgType string under question.
	 * @return the constant it represents. Return unknown if no constants 
	 * can be represented by the given string.
	 */
	public static MessageType getMessageType(String msgType) {
		if (isPublicMessage(msgType)) {
			return Public;
		} else if (isPrivateMessage(msgType)) {
			return Private;
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
	
	private static boolean isPublicMessage(String msg) { return msg.equalsIgnoreCase(Public.toString()); }
	private static boolean isPrivateMessage(String msg) { return msg.equalsIgnoreCase(Private.toString()); }
	private static boolean isConnectMessage(String msg) { return msg.equalsIgnoreCase(Connect.toString()); }
	private static boolean isDisconnectMessage(String msg) { return msg.equalsIgnoreCase(Disconnect.toString()); }
	private static boolean isDoneMessage(String msg) { return msg.equalsIgnoreCase(Done.toString()); }
	
	public static String constructServerMesaage(String msg) { return "Server~" + msg + "~Chat"; }
	public String toString() { return message; }
}
