package messaging;

import dialogs.Constants;

public class MessageUtils {
	
	private MessageUtils() { /* no instantiation */ }

    /**
     * Constructs a readable form of a message.
     * @param msg msg to construct information from.
     * @return formated message contents.
     */
    public static String readableMessageFormatForClient(Message msg) {
        return "[" + msg.getAuthor() + "]: " + msg.getContent() + "\n";
    }
    /**
     * Constructs a readable form of a message.
     * @param msg msg to construct information from.
     * @return formated message contents.
     */
    public static String readableMessageFormatForServer(Message msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("[" + msg.getAuthor() + "]: " + msg.getContent());
        sb.append(" (" + MessageUtils.getMessageType(msg).toString() + ") ");
        return sb.toString() + "\n";
    }

	/**
	 * Factory method for message instantiation.
	 * @param author the original creator of the message.
	 * @param content the significant information of the message.
	 * @param type the type of message.
	 * @return a message in its entirety.
	 */
	public static Message constructMessage(String author, String content, MessageType type) {
	    // messages should not have the delimiter in the content.
	    /*if (content.indexOf(Constants.DELIMITER) != -1) {
	        return new Message(author, Constants.EMPTY_STRING, type.getValueAsInt());
        }*/
		switch (type) {
			case Connect: return new Message(author, content, MessageType.Connect.getValueAsInt());
            case Disconnect: return new Message(author, content, MessageType.Disconnect.getValueAsInt());
            case Public: return new Message(author, content, MessageType.Public.getValueAsInt());
            case Command: return new Message(author, content, MessageType.Command.getValueAsInt());
			case Update: return new Message(author, content, MessageType.Update.getValueAsInt());
            case Draw: return new Message(author, content, MessageType.Draw.getValueAsInt());
            case Game: return new Message(author, content, MessageType.Game.getValueAsInt());
            default: return new Message(author, content, MessageType.Unsupported.getValueAsInt());
		}
	}

    /**
     * Given a raw message representation as a string, constructs a message.
     * @param messageBuild a string guaranteed to be a raw message string.
     * @return a fully constructed message.
     */
	public static Message parseMessageBuild(String messageBuild) {
	    int delimiters = numberOfDelimiters(messageBuild);
	    Message msg = constructMessage("SERVER", "UNSUPPORTED", MessageType.Unsupported);
	    switch (delimiters) {
            case Constants.DELIMITERS_FOR_MESSAGE: msg = new Message(messageBuild.split(Constants.REGEX_FOR_MESSAGE_DELIMITER));
        }
        return msg;
	}
    /**
     * Returns the message type coresponding to the given integer.
     * @param messageType the integer mapped to a message type.
     * @return MessageType that is mapped the the given int.
     */
	public static MessageType getMessageType(int messageType) {
	    for (MessageType type : MessageType.values()) {
	        if (messageType == type.getValueAsInt()) { return type; }
        }
        return MessageType.Unsupported;
    }
    /**
     * Returns the message type coresponding to the given integer.
     * @param messageType the integer mapped to a message type.
     * @return MessageType that is mapped the the given int.
     */
    public static MessageType getMessageType(String messageType) {
        if (messageType.length() > 1) { return MessageType.Unsupported; }
        int digit = messageType.charAt(0) - '0';
        for (MessageType type : MessageType.values()) {
            if (digit == type.getValueAsInt()) { return type; }
        }
        return MessageType.Unsupported;
    }
    /**
     * Returns the message type coresponding to the given integer.
     * @param msg the message to interperate
     * @return MessageType that is mapped the the given int.
     */
    public static MessageType getMessageType(Message msg) {
        return getMessageType(msg.getMessageTypeValueAsInt());
    }
    /**
     * Counts the number of delimiters in the given message build
     * @param messageBuild build of the message
     * @return the number of delimiters found.
     */
    private static int numberOfDelimiters(String messageBuild) {
        int count = 0;
        for (int i = 0; i < messageBuild.length(); i++) {
            if (messageBuild.charAt(i) == Constants.DELIMITER) {
                count++;
            }
        }
        return count;
    }
    /***
     * Determines if the given string is represenative of a command message or
     * just a regular message.
     * @param str the contents use to determine the message type.
     * @return The message type representing command or public messagetype.
     */
    public static MessageType isEitherCommandOrPublicMessage(String str) {
        if (str.toUpperCase().contains(MessageType.Command.toString().toUpperCase())) {
            return MessageType.Command;
        } else {
            return MessageType.Public;
        }
    }

    /**
     * Determines if the given stream of characters contains insufficient amount of delimiters to be parsed
     * @param stream the string from the / a source
     * @return true if there is not an adequate amount of delimiters.
     */
    public static boolean streamContainsInsufficientDelimiters(String stream) {
        return numberOfDelimiters(stream) == 1;
    }
}
