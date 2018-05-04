package network;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.control.TextArea;

public class Server implements Runnable {
	
	private int portNumber;
	private TextArea mainArea;
	private ServerSocket serverSocket;
	private ConcurrentHashMap<String, PrintWriter> map;
	
	public Server(ConcurrentHashMap<String, PrintWriter> chm, TextArea area, int port) {
		map = chm;
		mainArea = area;
		portNumber = port;
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			while (true) {
				Socket clientSocket = serverSocket.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
				Thread listener = new Thread(new SubServer(map, clientSocket, writer, mainArea));
				listener.start();
				mainArea.appendText("A user has connected.\n");
			}
		} catch (Exception ex) {
			mainArea.appendText("Error making connection.\n");
		}
	}
}