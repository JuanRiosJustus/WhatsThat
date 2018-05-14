package game;

public class Player {
	
	private static final int MAX_PHRASES = 2; 
	
	private String name;
	private int points;
	
	private String[] phrases = new String[MAX_PHRASES];
	
	private Player(String nme) {
		name = nme;
	}
	
	
	public String getName() { return name; }
	public String getFirstPhrase() { return phrases[0]; }
	public String getSecondPhrase() { return phrases[1]; }
}
