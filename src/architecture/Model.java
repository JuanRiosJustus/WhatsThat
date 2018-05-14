package architecture;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

import game.Game;
import network.IOStream;
import network.User;

public class Model {
	
	private boolean isServer; 
	private User userData;
	
	public Model(boolean isServer) { 
		this.isServer = isServer; 
	}
	
	public void handleIOStream(IOStream ios) {
		
	}
	/**
	 * Checks to see if the current model is representative of the server.
	 * @return true if and only if the model was set as the server object.
	 */
	public boolean isTheServer() { return isServer; }
	
}
