package network;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IOUHolder {
	
	private IOQueue queues = new IOQueue();
	private ConcurrentLinkedQueue<String> points = new ConcurrentLinkedQueue<>();
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

	/**
	 * Returns the map of users.
	 */
	public ConcurrentHashMap<String, User> getUsers() { return users; }
	
	public IOQueue getQueue() { return queues; }

	/**
	 * Returns the queue of strings which represent
	 * one or many canvas objects.
	 */
	public ConcurrentLinkedQueue<String> getCanvasObjects() { return points; }
}
