package network;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IOQueue {
	
	private ConcurrentLinkedQueue<Object> incomingQueue = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Object> outgoingQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * @return the stream of messages that is to be interpreted by the server.
	 */
	public ConcurrentLinkedQueue<Object> getInputQueue() { return incomingQueue; }
	/**
	 * @return the stream of messages to be sent to the players.
	 */
	public ConcurrentLinkedQueue<Object> getOutputQueue() { return outgoingQueue; }
	public Object pollInputQueue() { return incomingQueue.poll(); }
	
	
	
	
	public int containedOutgoingElements() { return outgoingQueue.size(); }
	public int containedIncomingElements() { return incomingQueue.size(); }
	
	public boolean containsIncomingElements() { return !incomingQueue.isEmpty(); }
	public boolean containsOutgoingElements() { return !outgoingQueue.isEmpty(); }

	public boolean enqueueFromExternal(Object obj) { return incomingQueue.offer(obj); }
	public Object dequeueToExternal() { return outgoingQueue.poll(); }

	public boolean enqueueFromInternal(Object obj) { return outgoingQueue.offer(obj); }
	public Object dequeueFromInternal() { return incomingQueue.poll(); }

	public Object dequeue() { return incomingQueue.poll(); }
	public boolean enqueue(Object obj) { return outgoingQueue.offer(obj); }
	public Object peek() { return incomingQueue.peek(); }
}
