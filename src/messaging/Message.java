package messaging;

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
}
