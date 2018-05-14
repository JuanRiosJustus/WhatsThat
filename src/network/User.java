package network;

import java.io.PrintWriter;

import utlility.StringUtility;

public class User {
	
	private PrintWriter stream;
	private boolean hosting;
	private String name;
	private String ID;
	
	public User(String nme, PrintWriter outStream, boolean host) {
		name = nme;
		stream = outStream;
		hosting = host;
		ID = StringUtility.generateRandomPin(4);
	}
	
	public String getName() { return name; }
	public String getID() { return ID; }
	public boolean isHost() { return hosting; }
	public void sendMessage(String str) { 
		if (stream != null) {
			stream.println(str); 
			stream.flush(); 
		}
	}
}
