package network;

import java.io.PrintWriter;

import utlility.StringUtils;

public class User {
	
	private PrintWriter stream;
	private boolean hosting;
	private String name;
	private String ID;
	
	public User(String nme, PrintWriter outStream, boolean host) {
		name = nme;
		stream = outStream;
		hosting = host;
		ID = StringUtils.generateRandomPin(4);
	}
	
	public User(String nme) { name = nme; }
	
	public String getName() { return name; }
	public String getID() { return ID; }
	public boolean isHost() { return hosting; }
	public void setHost() { hosting = true; }
	public void sendMessage(String str) { 
		if (stream != null) {
			stream.println(str); 
			stream.flush(); 
		}
	}
}
