package game;

public class Player {
	
	private String name;
	private int points;
	
	private String[] phrases = new String[2];
	
	private Player(String nme) {
		name = nme;
	}
	
	
	public String getName() { return name; }
	public String getFirstPhrase() { return phrases[0]; }
	public String getSecondPhrase() { return phrases[1]; }
}
