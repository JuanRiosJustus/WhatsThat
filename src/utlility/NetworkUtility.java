package utlility;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;

public class NetworkUtility {
	
	/**
	 * Determines if the given integer is a valid
	 * number to represent a port number.
	 * 
	 * @param portNumber number to check.
	 * @return true if and only if the number is between
	 * the values 0 and 65535 exclusive.
	 */
	public static boolean isValidPort(int portNumber) {
		return portNumber > 0 && portNumber < 65535;
	}
	
	/**
	 * Determines if the given String can represent 
	 * a valid IP address. 
	 * 
	 * @param address string to check.
	 * @return true if and only if the given string is 
	 * not null, contains a valid amount of periods,
	 * and contains no non-digit characters.
	 */
	public static boolean isValidIP(String address) {
		if (address == null || address.length() < 1) {
			return false;
		}
		String[] splits = address.split("\\.");
		if (splits.length != 4) { return false; }
		for (String num : splits) {
			for (char  c : num.toCharArray()) {
				if (Character.isDigit(c) == false) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * @return The local host address.
	 */
	public static String getHostAddress() { 
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "Error fetching address.";
		} 
	}
	/**
	 * @return The address and display name
	 * of all network interfaces of the current device.
	 */
	public static String getHostAddresses() {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("Showing all Network Interfaces...\n");
			sb.append("The suggested address is " + InetAddress.getLocalHost().getHostAddress() + "\n");
		    Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
		    while (n.hasMoreElements()) {
		        NetworkInterface e = n.nextElement();
		        Enumeration<InetAddress> a = e.getInetAddresses();
		        while (a.hasMoreElements()) {
		            InetAddress addr = a.nextElement();
		            sb.append("(" + e.getDisplayName() + "): " + addr.getHostAddress() + " (" + e.getName() + ") \n");
		        }
		    }
		} catch (Exception ex) {
			sb.append("Fatal network error: " + ex.getMessage());
		}
	    return sb.toString();
	}
	/**
	 * Given a string, determines if the given string
	 * does not contain the character used to parse; '~'.
	 * 
	 * @param msg Message to determine if is valid to send
	 * @return true if the index of the parse character
	 * was not found within the given string.
	 */
	public static boolean isValidMessage(String msg) { 
		if (msg == null || msg.length() < 1) { return false; }
		return msg.indexOf('~') == -1;
	}
	
	public static boolean isHostAvailable(String address, int port) {
		try {
			InetSocketAddress sa = new InetSocketAddress(address, port);
			Socket sc = new Socket();
			sc.connect(sa, 3);
			sc.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
