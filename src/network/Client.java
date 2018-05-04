package network;

import java.io.BufferedReader;
import java.util.ArrayList;

import javafx.scene.control.TextArea;
import messaging.Message;

public class Client implements Runnable {

	private TextArea chatArea;
	private ArrayList<String> users;
	private BufferedReader reader;
	
	public Client(TextArea ta, ArrayList<String> list, BufferedReader br) {
		chatArea = ta;
		users = list;
		reader = br;
	}
	
	@Override
	public void run() {
		String stream = null;
		Message msg = null;
		
		try {
			while ((stream = reader.readLine()) != null) {
				
				msg = new Message(stream);
				
				switch (msg.getType()) {
					case Chat: {
						chatArea.appendText("[" + msg.getSender() + "]: " + msg.getContent() + "\n");
						chatArea.positionCaret(chatArea.getText().length());
					} break;
					case Connect: {
						users.add(msg.getSender());
					} break;
					case Disconnect: {
	                    users.remove(msg.getSender());
	                    chatArea.appendText(msg.getSender() + " has disconnected.\n");
	                    chatArea.positionCaret(chatArea.getText().length());
					} break;
					default: {
						// TODO make sure to do this later...
					} break;
				}
			}
		} catch (Exception ex) {
			chatArea.appendText("Error connecting to server...\n");
		}
	}

}
