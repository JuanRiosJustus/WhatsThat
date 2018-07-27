package architecture;

import game.Game;
import responsesRequests.GameStartRequest;

public class Model {
	
	private boolean isServer; 
	private Game game;
	
	public Model(boolean isServer) { 
		this.isServer = isServer; 
	}
	
	/**
	 * // TODO display the players and topic back to the players
	 * Initializes the game object of given the command and its arguments.
	 * @param gsr the raw command that is used for the game
	 */
	public String handleGameStartRequest(GameStartRequest gsr) {
	    if (game != null && game.isCurrentlyOngoing()) { return "Game is already running..."; }
	    game = new Game(gsr.getPlayers(), gsr.getTopic(), gsr.getRounds());
	    if (game.isCurrentlyOngoing()) {
	        return "Game has started!";
	    } else {
	        return "Game unable to start...";
	    }
    }
	/**
	 * Checks to see if the current model is representative of the server.
	 * @return true if and only if the model was set as the server object.
	 */
	public boolean isTheServer() { return isServer; }
	public boolean isGameOngoing() { return game.isCurrentlyOngoing(); }
	public String getGameTopic() { return game.getGameTopic(); }
	public String getGameLeader() { return game.getLeader(); }
	public String getGamePlayers() { return game.getPlayers(); }
	
}
