package architecture;

import game.Game;

public class Model {
	
	private boolean isServer; 
	private Game game = new Game();
	
	public Model(boolean isServer) { this.isServer = isServer; }
	
	public void initializeServerComponents() {
		
	}
	public void initializeClientComponents() {
		
	}
	
	public boolean isTheServer() { return isServer; }
}
