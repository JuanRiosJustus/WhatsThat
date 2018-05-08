package game;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	private String leader;
	private ArrayList<String> players;
	private int maxPlayersAllowed;
	private boolean isInPlay;
	private int state;
	private Random generator = new Random();
	
	
	public Game(int mpa) {
		maxPlayersAllowed = mpa;
	}
	
	public void initGame(ArrayList<String> playrs) {
		// set the game in play
		isInPlay = true;
		// add all the players to the game.
		this.players = playrs;
		// select a random player to be the leader.
		leader = selectRandomPlayer();
		// set game state to next stage
		state = 1;
	}
	
	public void handleGameState() {
		switch (state) {
			case 1:  
			default: return;
		}
	}
	public void enqueueUserSubmissions() {
		// 1.) users offer two strings that must be unique
		// 2.) randomly select a string from a user
			// 2.5) delete one of every users string besides
			// the selected string, thus deleting that users other string
		// 3.) 
		
	}
	
	public boolean isLeader(String name) { return leader.equals(name); }
	public boolean isPlayer(String name) { return players.contains(name); }
	public void addPlayer(String name) { if (isPlayer(name) == false) { players.add(name); } }
	public String selectRandomPlayer() { return players.get(generator.nextInt(players.size())); }
	public String getLeader() { return leader; }
}
