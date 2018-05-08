package architecture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.control.TextArea;
import messaging.Message;
import messaging.MessageType;
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
	
	/* Server specific instance variables */
	private Server server;
	private Thread thread;
	private boolean isRunning;
	private ConcurrentHashMap<String, PrintWriter> map;
	
	
	public Controller(Model newModel, View newView) {
		model = newModel;
		view = newView;
	}
	public void initializeUsername(String name) { username = name; }
	
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
	
	
	public void sendMessage(String msg) {
		if (msg == null || msg.length() < 1) { return; }
		message(msg.trim().replace("~", ""));
	}
	

	// TODO
	private void message(String msg) {
		if (Message.isCorrectlyFormatedPrivateMessage(msg)) {
			sendPrivateMessage(msg);
		} else {
			sendPublicMessage(msg);
		}
	}
	private void sendPublicMessage(String msg) {
		try {
			writer.println(username + "~" + msg + "~" + MessageType.Public.toString());
			writer.flush();
		} catch (Exception ex) {
			// TODO error
			view.appendTextToChat("Error sending message.");
		}
	}
	private void sendPrivateMessage(String msg) {
		try {
			writer.println(username + "~" + msg + "~" + MessageType.Private.toString());
			writer.flush();
		} catch (Exception ex) {
			// TODO error
			view.appendTextToChat("Error sending message.");
		}
	}
	public void terminateClientConnection() { sendDisconnectMessage(); disconnect(); }
	
	private void sendDisconnectMessage() {
		try {
			writer.println(username + "~ has disconnected.~" + MessageType.Disconnect.toString());
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
	
	public BufferedReader getBufferedReader() { return reader; }
	public PrintWriter getPrintWriter() { return writer; }
	public String getUsername() { return username; }
	public boolean isConnectedToServer() { return isConnected; }
	public boolean isServerRunning() { return isRunning; }
}
