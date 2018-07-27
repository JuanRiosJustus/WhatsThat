package messaging;

import dialogs.Constants;

public class Message {

    protected String[] contents;
    protected String base;

    /**
     * Default constructor used to create an raw message.
     * @param author the raw constructor of the message.
     * @param content the significant information of the message.
     * @param messageType the type of the message.
     */
	public Message(String author, String content, int messageType) {
	    base = init(author, content, messageType);
	}
	public Message(String[] args) {
	    if (args.length == 3) {
	    	init(args[0], args[1], args[2]);
		} else {
	    	init("Unknown", "Null", -1);
		}
	}
    /**
     * Constructs a formatted message in the given form below
	 * "[MessageAuthor|MessageContent|MessageType]
     * @param author the author/creator/author of the message.
     * @param main the significant information of the message.
     * @param messageType the type of the message.
     * @return the string representation of the message.
     */
	protected String init(String author, String main, int messageType) {
	    if (contents == null) {
	        contents = new String[3];
	        contents[0] = author;
            contents[1] = main;
	        contents[2] = messageType + "";
	        return build();
        } else {
	        return base;
        }
	}
	/**
     * Constructs a formatted message in the given form below
     * "[MessageAuthor|MessageContent|MessageType]
     * @param author the author/creator/author of the message.
     * @param main the significant information of the message.
     * @param messageType the type of the message.
     * @return the string representation of the message.
     */
    protected String init(String author, String main, String messageType) {
        if (contents == null) {
            contents = new String[3];
            contents[0] = author;
            contents[1] = main;
            contents[2] = messageType;
            return build();
        } else {
            return base;
        }
    }

	public String getAuthor() {
	    return contents[0];
	}
	public String getContent() { return contents[1]; }
	public String getMessageTypeValueAsString() { return contents[2]; }
	public int getMessageTypeValueAsInt() { return contents[2].charAt(0) - '0'; }
	public void emptyContents() { contents[1] = Constants.EMPTY_STRING; }
    /**
     * Formalizes the contents of the string.
     * @return String representing the contents of the string.
     */
	public String build() {
	    if (base == null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < contents.length; i++) {
                sb.append(contents[i]);
                if (i + 1 != contents.length) { sb.append(Constants.DELIMITER); }
            }
            base = sb.toString();
            return base;
        } else {
	        return base;
        }
	}

	public String toString() { return build(); }
}
