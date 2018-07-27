package commands;

public enum CommandType {

    StartGame("StartGame"),
    EndGame("EndGame"),
    //Settings("Settings"),
    //Status("Status"),
    //Scoreboard("Scoreboard"),
    Unsupported("Unsupported");

    private final String commandName;

    CommandType(String gameCMD) {
        commandName = gameCMD;
    }

    public String toString() { return commandName; }
}
