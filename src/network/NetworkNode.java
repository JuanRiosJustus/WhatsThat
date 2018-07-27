package network;
import java.io.BufferedReader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkNode {
	
	protected BufferedReader input;
	protected ConcurrentLinkedQueue<String> canvasObjects;
	protected IOQueue ioqueue;
	protected ConcurrentHashMap<String, User> usermap;
	
	public NetworkNode(BufferedReader br, IOUHolder holder) {
		input = br;
		ioqueue = holder.getQueue();
		usermap = holder.getUsers();
		canvasObjects = holder.getCanvasObjects();
	}
}
