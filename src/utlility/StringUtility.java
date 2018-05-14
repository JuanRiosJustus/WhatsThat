package utlility;

import java.util.Random;

public class StringUtility {
	
	public static Random generator = new Random();
	public static String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	/**
	 * Determines if a string is equivalent to a numeric value,
	 * that is it contains no non-digit characters.
	 * 
	 * Space: O(1).
	 * Time: 
	 * @param str the string to determine
	 * @return true if and only if the string can
	 * be represented as a numerical value.
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.length() < 1) { return false; }
		if (str.charAt(0) == '-') { str = str.substring(1); }
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c) == false) { return false; }
		}
		return true;
	}
	/**
	 * 
	 * @param array the array of strings to look through.
	 * @param pattern the string to look for.
	 * @return true if and only if the pattern was found within the array
	 * where the case does not matter.
	 */
	public static boolean arrayContainsIgnoreCase(String[] array, String pattern) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equalsIgnoreCase(pattern)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isValidMessage(String msg) {
		return msg != null && msg.length() > 1 && msg.contains("~") == false; 
	}
	/**
	 * Generates a random pin based upper and lower case letters including digits.
	 * @param length
	 * @return
	 */
	public static String generateRandomPin(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int index = generator.nextInt(characters.length());
			sb.append(characters.charAt(index));
		}
		return sb.toString();
	}
}
