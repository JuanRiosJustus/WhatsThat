package userface;

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
}
