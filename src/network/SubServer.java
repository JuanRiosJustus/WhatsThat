package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import architecture.Controller;
import javafx.scene.control.TextArea;
import messaging.Message;
import messaging.MessageType;
import utlility.NetworkUtility;
import utlility.StringUtility;

public class SubServer implements Runnable {

	private TextArea mainArea;
	private PrintWriter writer;
	private BufferedReader reader;
	private IOStream ios;
	
	public SubServer(IOStream str, Socket s, PrintWriter pw, TextArea a) {
		writer = pw;
		ios = str;
		mainArea = a;
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception ex) {
			mainArea.appendText("Unexpected error occurred...\n");
		}
	}
	
	
	@Override
	public void run() {
		String message = null;
		Message msg = null;
		
		try {
			while ((message = reader.readLine()) != null) {
				
				msg = new Message(message);
				
				switch(msg.getType()) {
					case Connect: {
						// TODO this should check if the user has a name
						if (ios.getUsers().contains(msg.getSender())) {
							String newName = msg.getSender() + StringUtility.generateRandomPin(5);
							User usr = new User(newName, writer, ios.getUsers().isEmpty());
							usr.sendMessage(MessageType.constructServerFinalizationMessage(newName)); // forces client to rename themselves
							announce(newName + "~" + msg.getContent() + "~" + MessageType.Public.toString());
							addUser(newName);
	                        ios.getUsers().put(newName, usr);
						} else {
							announce(msg.getSender() + "~" + msg.getContent() + "~" + MessageType.Public.toString());
							addUser(msg.getSender());
							User usr = new User(msg.getSender(), writer, ios.getUsers().isEmpty());
	                        ios.getUsers().put(msg.getSender(), usr);
						}
					} break;
					case Disconnect: {
						announce(msg.getSender() + "~has disconnected.~" + MessageType.Public.toString());
                        removeUser(msg.getSender());
					} break;
					case Public: {
						announce(message);
					} break;
					case Private: {
						// the message will contain the string who represents the receiver to send it to.
						// if the message begins with someName and has a series of commas
						announceTo(message);
					} break;
					case Game: {
						// do game Stuff
						//announce(msg.getSender() + "~NULL~GameOn");
					}
					default: {
						// TODO man, this needs to be done later...
					} break;
				}
				
			}
		} catch (Exception ex) {
			mainArea.appendText("A connection was lost.\n");
			if (msg != null) { ios.getUsers().remove(msg.getSender()); }
		}
	}
	
	private void announceTo(String msg) {
		String[] content = msg.split("~");
		if (Message.isCorrectlyFormatedPrivateMessage(content[1]) == false) {
			mainArea.appendText("**" + content[0] + "** failed to send a private message.\n");
			return;
		}
		String[] recipients = Message.getPrivateMessageRecipients(content[1]);
		if (recipients == null) { return; }
		try {
			Iterator<Entry<String, User>> iter = ios.getUsers().entrySet().iterator(); //map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, User> e = iter.next();
				// check to see if the current nodes name, is a recipient
				if (StringUtility.arrayContainsIgnoreCase(recipients, e.getKey())) { 
					mainArea.appendText("[" + content[0] + "] -> " + "[" + e.getKey() + "]: " + content[1]);
					e.getValue().sendMessage(msg);
				}
			}
		} catch (Exception ex) {
			mainArea.appendText("Error sending message to recipient(s");
		}
	}
	private void announce(String message) {
		Iterator<Entry<String, User>> it = ios.getUsers().entrySet().iterator(); //map.entrySet().iterator();
		String[] content = message.split("~");
		mainArea.appendText("[" + content[0] + "]: " + content[1] + "\n");
		while (it.hasNext()) {
			try {
				Entry<String, User> r = it.next();
				r.getValue().sendMessage(message);
			} catch (Exception ex) {
				mainArea.appendText("Error sending message to clients...\n");
			}
		}
	}
	private void removeUser(String givenUsername) {
		ios.getUsers().remove(givenUsername);
		mainArea.appendText("[" + givenUsername + "] has been removed.\n");
		mainArea.appendText("Telling users to remove [" + givenUsername + "]\n");
		mainArea.positionCaret(mainArea.getLength());
		announce(givenUsername + MessageType.JustDisconnected.toString());
		announce(MessageType.ServerActionsCompleted.toString());
	}
	private void addUser(String givenUsername) {
		mainArea.appendText(givenUsername + " is connected.\n");
		mainArea.appendText("Telling users to add [" + givenUsername + "]\n");
		mainArea.positionCaret(mainArea.getLength());
		announce(givenUsername + MessageType.JustConnected.toString());
		announce(MessageType.ServerActionsCompleted.toString());
	}
}