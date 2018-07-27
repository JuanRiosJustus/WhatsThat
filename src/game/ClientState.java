package game;

public enum ClientState {

    InLobby("InLobby"),
    InGamePreMain("InGamePreMain"),
    InGameMain("InGameMain"),
    InGamePostMain("InGamePostMain");

    private final String state;

    ClientState(String str) {
        state = str;
    }

    public String toString() { return state; }
}
