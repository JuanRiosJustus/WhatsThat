package messaging;

public class MessageFactory {
	
	public static String constructMessage(String username, String message, MessageType msgType) {
		switch (msgType) {
			case Connect: return username + "~" + message + "~" + MessageType.Connect.toString();
			case Disconnect: return username + "~" + message + "~" + MessageType.Disconnect.toString();
			case Finalize: return username + "~" + message + "~" + MessageType.Finalize.toString();
			case Public: return username + "~" + message + "~" + MessageType.Public.toString();
			case Private: return username + "~" + message + "~" + MessageType.Private.toString();
			case Done: return username + "~" + message + "~" + MessageType.Done.toString();
			default: return username + "~" + message + "~" + MessageType.Unknown.toString();
		}
	}
}
