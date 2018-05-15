package architecture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.control.TextArea;
import messaging.Message;
import messaging.MessageFactory;
import messaging.MessageType;
import network.IOStream;
import network.Server;
import utlility.NetworkUtility;

public class Controller {
	
	private Model model;
	private View view;
	
	/* Client Specific instance variables */
	private Socket connection;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean isConnected;
	private String username;
	
	private boolean isRunning;
	
	/* Input and Output Helper Class*/
	private IOStream inputOutputStream;
	
	public Controller(Model newModel, View newView) {
		model = newModel;
		view = newView;
		inputOutputStream = new IOStream();
	}
	
	public boolean initializeClientConnection(String address, int port) {	
		try {
			connection = new Socket(address, port);
			InputStreamReader isr = new InputStreamReader(connection.getInputStream());
			reader = new BufferedReader(isr);
			writer = new PrintWriter(connection.getOutputStream());
			writer.println(username + "~ has connected.~" + MessageType.Connect.toString());
			writer.flush();
			view.appendTextToChat("Connected to server.");
			isConnected = true;
			return true;
		} catch (Exception ex) {
			view.appendTextToChat(ex.getMessage());
			view.appendTextToChat("Unable to connect.");
			return false;
		}
	}
	
	public void handleIOStreams(IOStream str) {
		model.handleIOStream(str);
	}
	
	public void sendMessage(String msg) {
		if (msg == null || msg.length() < 1) { return; }
		message(msg.trim().replace("~", ""));
	}
	
	private void message(String msg) {
		if (Message.isCorrectlyFormatedPrivateMessage(msg)) {
			sendPrivateMessage(msg);
		} else {
			sendPublicMessage(msg);
		}
	}
	
	/**
	 * Sends a public message tot he server. 
	 * @param msg message to be sent to the server.
	 */
	private void sendPublicMessage(String msg) {
		try {
			writer.println(MessageFactory.constructMessage(username, msg, MessageType.Public));
			writer.flush();
		} catch (Exception ex) {
			view.appendTextToChat("Error sending message...");
		}
	}
	/**
	 * Sends a private message to the server.
	 * @param msg the message to be sent to the server.
	 */
	private void sendPrivateMessage(String msg) {
		try {
			writer.println(MessageFactory.constructMessage(username, msg, MessageType.Private));
			writer.flush();
		} catch (Exception ex) {
			view.appendTextToChat("Error sending message...");
		}
	}
	public void terminateClientConnection() { sendDisconnectMessage(); disconnect(); }
	
	private void sendDisconnectMessage() {
		try {
			writer.println(MessageFactory.constructMessage(username, " has disconnected.", MessageType.Disconnect));
			writer.flush();
		} catch (Exception ex) {
			view.appendTextToChat("Error sending disconnect message...");
		}
	}
	private void disconnect() {
		try {
			connection.close();
			isConnected = false;
			view.appendTextToChat("Disconnected from server.");
		} catch (Exception ex) {
			view.appendTextToChat("Error disconnecting from server...");
		}
	}

	public void startServer() { isRunning = true; }
	public void stopServer() { isRunning = false; }
	public void setUsername(String name) { username = name; }
	public BufferedReader getBufferedReader() { return reader; }
	public PrintWriter getPrintWriter() { return writer; }
	public String getUsername() { return username; }
	public IOStream getIOStream() { return inputOutputStream; }
	public boolean isConnectedToServer() { return isConnected; }
	public boolean isServerRunning() { return isRunning; }
}
