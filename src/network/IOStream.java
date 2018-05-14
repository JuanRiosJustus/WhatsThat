package network;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOStream {
	
	private ConcurrentLinkedQueue<String> inQueue = new ConcurrentLinkedQueue<>();;
	private ConcurrentLinkedQueue<String> outQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
	
	/**
	 * @return the stream of messages that is to be interpreted by the server.
	 */
	public ConcurrentLinkedQueue<String> getInputQueue() { return inQueue; }
	/**
	 * @return the stream of messages to be sent to the players.
	 */
	public ConcurrentLinkedQueue<String> getOutputQueue() { return outQueue; }
	/**
	 * @return the lobby of users that are in the game.
	 */
	public ConcurrentHashMap<String, User> getUsers() { return users; }
}
