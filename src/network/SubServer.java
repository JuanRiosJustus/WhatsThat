package network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

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
	private ConcurrentHashMap<String, PrintWriter> map;
	
	public SubServer(ConcurrentHashMap<String, PrintWriter> chm, Socket s, PrintWriter pw, TextArea a) {
		writer = pw;
		map = chm;
		mainArea = a;
		try {
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (Exception ex) {
			mainArea.appendText("Unexpected error occurred...\n");
		}
	}
	
	@Override
	public void run() {
		String message;
		Message msg = null;
		
		try {
			while ((message = reader.readLine()) != null) {
				
				msg = new Message(message);
				
				switch(msg.getType()) {
					case Connect: {
						announce(msg.getSender() + "~" + msg.getContent() + "~" + MessageType.Public.toString());
						addUser(msg.getSender());
                        map.put(msg.getSender(), writer);
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
			if (msg != null) { map.remove(msg.getSender()); }
		}
	}
	private String checkAndHandleIfTaken(String name) {
		// handle the username such that a new username is sent to the user if taken
		if (map.contains(name) == false) {
			return name + "(taken)";
		}
		return name;
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
			Iterator<Entry<String, PrintWriter>> iter = map.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, PrintWriter> e = iter.next();
				// check to see if the current nodes name, is a recipient
				if (StringUtility.arrayContainsIgnoreCase(recipients, e.getKey())) { 
					mainArea.appendText("[" + content[0] + "] -> " + "[" + e.getKey() + "]: " + content[1]);
					e.getValue().println(msg);
					e.getValue().flush();
				}
			}
		} catch (Exception ex) {
			mainArea.appendText("Error sending message to recipient(s");
		}
	}
	private void announce(String message) {
		Iterator<Entry<String, PrintWriter>> it = map.entrySet().iterator();
		String[] content = message.split("~");
		mainArea.appendText("[" + content[0] + "]: " + content[1] + "\n");
		while (it.hasNext()) {
			try {
				Entry<String, PrintWriter> r = it.next();
				PrintWriter currentClient = r.getValue();
				currentClient.println(message);
				currentClient.flush();
			} catch (Exception ex) {
				mainArea.appendText("Error sending message to clients...\n");
			}
		}
	}
	private void removeUser(String givenUsername) {
		map.remove(givenUsername);
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