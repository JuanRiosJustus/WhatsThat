package network;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import dialogs.Constants;
import messaging.*;

public class Network {

    private final static String COMMAND_SIGNIFIER = "COMMAND";
	/**
	 * Sends a formatted message to all users within the hashmap.
	 * 
	 * @param userMap the map of users that is to be checked.
	 * @param msg the formated 
	 */
	public void announceMessagePublicly(ConcurrentHashMap<String, User> userMap, Message msg) {
		if (msg == null) { return; }
		Iterator<Entry<String, User>> iter = userMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, User> current = iter.next();
			current.getValue().sendMessage(msg.toString());
		}
	}/**
	 * Sends a formatted message to all users within the hashmap.
	 *
	 * @param userMap the map of users that is to be checked.
	 * @param msg the formated
	 */
	public void announceMessageContentPublicly(ConcurrentHashMap<String, User> userMap, Message msg) {
		Iterator<Entry<String, User>> iter = userMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, User> current = iter.next();
			current.getValue().sendMessage(msg.getContent());
		}
	}
	/**
	 * Given a message, as long as the message contains a substring of a user, sends the user that message
	 * 
	 * @param userMap the map of users that we select from.
	 * @param msg the message with contents to send to a user.
	 * @return returns true if and only if a message was sent to at least one person.
	 */
	public boolean announceMessagePrivately(ConcurrentHashMap<String, User> userMap, Message msg) {
		Iterator<Entry<String, User>> iter = userMap.entrySet().iterator();
		String comprableMSG = msg.getContent().toLowerCase();
		boolean hasSent = false;
		// find the users that need to be sent the message.
		while (iter.hasNext()) {
			Entry<String, User> t = iter.next();
			String currentUser = t.getKey().toLowerCase();
			if (comprableMSG.contains(currentUser)) {
				t.getValue().sendMessage(msg.toString());
				hasSent = true;
			}
		}
		return hasSent;
	}
	/**
	 * Announces to all users that they must add the given user because they connected.
	 * 
	 * @param userMap the map of users that must add the given user.
	 * @param userToAdd the user that needs to be added.
	 */
	public void announceConnectionMessage(ConcurrentHashMap<String, User> userMap, String userToAdd) {
		//String msg = MessageUtils.constructMessage(userToAdd, "announceToAdd", MessageType.Connect);
		Message toSend = MessageUtils.constructMessage(userToAdd, " CONNECTED! ", MessageType.Connect);
		announceMessagePublicly(userMap, toSend);
	}
	/**
	 * Announces to all users that they must remove the given user because theyre disconnected..
	 * 
	 * @param userMap the map of users that must be told to remove the given user.
	 * @param userToRemove the user that needs to be removed.
	 */
	public void announceDisconnectionMessage(ConcurrentHashMap<String, User> userMap, String userToRemove) {
		Message toSend = MessageUtils.constructMessage(userToRemove, " DISCONNECTED! ", MessageType.Disconnect);
		//String msg = MessageUtils.constructMessage(userToRemove, "announceToRemove", MessageType.Disconnect);
		announceMessagePublicly(userMap, toSend);
	}
	public void announceUpdateMessage(User usr, String content) {
		Message msg = MessageUtils.constructMessage("SERVER", content, MessageType.Update);
		usr.sendMessage(msg.build());
	}

	/**
	 * Sends the string value of content where the content
	 * contains
	 * A point, where the content is displayed on canvas
	 * A color,
	 * A url link or text
	 * @param os
	 * @param content
	 * @return
	 */
	public boolean sendCanvasObjectsToServer(PrintWriter os, String content, String username) {
		Message msg = MessageUtils.constructMessage(username, content, MessageType.Draw);
		return sendPublicMessage(os, msg);
	}

    /**
     * TODO TOTEST
     * Sends a message to the server
     * @param msg
     */
    public boolean sendStringToServer(PrintWriter outStream, String username, String content) {
        MessageType type = MessageUtils.isEitherCommandOrPublicMessage(content);
        Message msg = MessageUtils.constructMessage(username, content, type);
        if (content.toUpperCase().contains(COMMAND_SIGNIFIER)) {
            return sendCommandMessage(outStream, msg);
        } else {
            return sendPublicMessage(outStream, msg);
        }
    }
    // sends a command message to the server, where a command message
    // has the command token inside its contents.
    private boolean sendCommandMessage(PrintWriter writer, Message msg) {
        try {
            writer.println(msg.toString());
            writer.flush();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    /**
     * Sends a public message to the server.
     * @param msg message to be sent to the server.
     */
    private boolean sendPublicMessage(PrintWriter writer, Message msg) {
        try {
            writer.println(msg.toString());
            writer.flush();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
