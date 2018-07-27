package game;


public class Stopwatch {

	private static final long NULL = -1;
	private long startTime = NULL;
	private long endTime = NULL;

	/**
	 * Starts the stopwatch time tracker.
	 */
	public void start() {
		startTime = System.currentTimeMillis();
	}
	/**
	 * Stops the stopwatch time tracker, where it can only be stopped, if
	 * start has been invoked.
	 */
	public void stop() {
		if (startTime == NULL) { return; }
		endTime = System.currentTimeMillis();
	}
	/**
	 * Gets the current duration of time since start has been invoked.
	 * @return the current elapsed time in milis.
	 */
	public long elapsedTimeMils() {
		if (startTime == NULL) {
			return NULL;
		} else {
			if (endTime == NULL) {
				return System.currentTimeMillis() - startTime;
			} else {
				return endTime - startTime;
			}
		}
	}
	/**
	 * Gets the current duration of time since start has been invoked.
	 * @return the current elapsed time in milis.
	 */
	public long elapsedTimeSecs() {
		if (startTime == NULL) {
			return NULL;
		} else {
			if (endTime == NULL) {
				return (System.currentTimeMillis() - startTime) / 1000;
			} else {
				return (endTime - startTime) / 1000;
			}
		}
	}
	/**
	 * resets the state to initial, where nothing has been tracked.
	 */
	public void reset() {
		startTime = NULL;
		endTime = NULL;
	}
}