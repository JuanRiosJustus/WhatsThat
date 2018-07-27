package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import network.User;

public class Game {
	
	private String leader;
	private String[] players;
	private String gameTopic;
	private boolean isOngoing;
	private int rounds;
	private Random random;
	
	public Game(String[] plyrs, String topic, int rnds ) {
	    random = new Random();
		players = plyrs;
		gameTopic = topic;
		rounds = rnds;
		initializeInitialGameState();
		isOngoing = true;
	}
	
	private void initializeInitialGameState() {
		leader = players[random.nextInt(players.length)];
	}
	public void enqueueUserSubmissions() {
		// 1.) users offer two strings that must be unique
		// 2.) randomly select a string from a user
			// 2.5) delete one of every users string besides
			// the selected string, thus deleting that users other string
		// 3.) 
	}
	public boolean isCurrentlyOngoing() { return isOngoing; }
	public String getPlayers() { return Arrays.toString(players); }
	public String getGameTopic() { return gameTopic; }
	public String getLeader() { return leader; }
}
