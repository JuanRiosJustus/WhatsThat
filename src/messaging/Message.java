package messaging;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import network.User;

public class Message {
	
	private String sender;
	private String content;
	private MessageType type;
	
	/**
	 * The default constructor where the 
	 * given message guaranteed to be formatted correctly.
	 * @param rawMessage message to parse.
	 */
	public Message(String rawMessage) {
		String[] data = rawMessage.split("~");
		sender = data[0];
		content = data[1];
		type = MessageType.getMessageType(data[2]);
	}
	
	public String getSender() { return sender; }
	public String getContent() { return content; }
	public MessageType getType() { return type; }
	
	/**
	 * Checks to see if the given string starts with private.
	 * @param str the string to check.
	 * @return true if and only if the string starts with character
	 * which is not case sensitive and must contain a colon.
	 */
	public static boolean isCorrectlyFormatedPrivateMessage(String str) {
		for (int i = 0; i < MessageType.Private.toString().length(); i++) {
			if (Character.toLowerCase(str.charAt(i)) != 
					Character.toLowerCase(MessageType.Private.toString().charAt(i))) {
				return false;
			}
		}
		int indexOfColon = str.indexOf(":");
		if (indexOfColon == -1) { return false; }
		for (int i = MessageType.Private.toString().length(); i < indexOfColon; i++) {
			if (Character.isWhitespace(str.charAt(i)) == false) { return true; }
		}
		return false;
	}
	/**
	 * Returns an array with all recipient names if the given message 
	 * has been formated correctly.
	 * @param msg the string to interpret.
	 * @return null if and only if the msg is not formated correctly.
	 */
	public static String[] getPrivateMessageRecipients(String msg) {
		if (isCorrectlyFormatedPrivateMessage(msg) == false) { return null; }
		String str = msg.substring(MessageType.Private.toString().length(), msg.indexOf(":"));
		return str.replace(" ", "").split(",");
	}
	public static boolean isFinalizationMessage(String msg) {
		if (msg == null || msg.length() < 1) { return false; }
		String[] args = msg.split("~");
		if (args == null || args.length != 3) { return false; }
		return msg.contains(MessageType.Finalize.toString());
	}
}
