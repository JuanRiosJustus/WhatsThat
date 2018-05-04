package game;

import java.util.ArrayList;
import java.util.Random;

public class Game {
	
	private String leader;
	private ArrayList<String> players = new ArrayList<String>();
	private Random generator = new Random();
	
	public boolean isLeader(String name) { return leader.equals(name); }
	public boolean isPlayer(String name) { return players.contains(name); }
	public void addPlayer(String name) { if (isPlayer(name) == false) { players.add(name); } }
	public String selectRandomPlayer() { return players.get(generator.nextInt(players.size())); }
	
}
