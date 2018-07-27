package utlility;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class Instructions {
	public static String howToSetup() {
		StringBuilder sb = new StringBuilder();
		sb.append("To get started, enter the address to connect to \n");
		sb.append("along with the username you wish to go by. \n");
		return sb.toString();
	}
	public static String howToMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("To send a private message, Type \"private <recipient><:><msg>\"");
		return sb.toString();
	}
	public static String messageToHostOfLobby() {
		StringBuilder sb = new StringBuilder();
		sb.append("You are the \"Host\" of the lobby\n");
		sb.append("to start the game, type \"!gamestart\"");
		return sb.toString();
	}
	public static String howToUseCommands() {
		StringBuilder sb = new StringBuilder();
		sb.append("To use a command, type <command><commandName><...params...>\n");
		sb.append("To start the game, type \"command startgame <no_space_topic> rounds\"\n");
		sb.append("There must be no spaces inside the topic\n");
		sb.append("The rounds must be a positive integer < 2^32\n");
		return sb.toString();
	}
}
