package game;

public class Stopwatch {
	
	private long startTime;
	private long duration;
	
	public void start() { startTime = System.currentTimeMillis(); }
	public void stop() { duration = System.currentTimeMillis() - startTime; }
	public int getDuration() { return (int)duration / 1000; }
}