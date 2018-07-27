package utlility;

import dialogs.Constants;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class StringUtils {
	
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
	/**
	 * Generates a random string represenatitive of a name using the given length.
	 * @param length size of the string.
	 * @return the with the given length.
	 */
	public static String generateRandomName(int length) {
		StringBuilder sb = new StringBuilder();
		String vowels = "aeiouyAEIOUY";
		String letters = characters.substring(0, characters.length() - 10);
		for (int i = 0; i < length; i++) {
			if (i % 2 == 0) {
				sb.append(letters.charAt(generator.nextInt(letters.length())));
			} else {
				sb.append(vowels.charAt(generator.nextInt(vowels.length())));
			}
		}
		return sb.toString();
	}

    /**
     * Checks to see fi the given string contains a delimiter.
     * @param str the string to be checked.
     * @return true if and only if the delimiter is not found.
     */
	public static boolean containsDelimiter(String str) {
	    for (int i = 0; i < str.length(); i++) {
	        if (str.charAt(i) == Constants.DELIMITER) {
	            return true;
            }
        }
        return false;
    }

    /**
     * Counts the number of given times the given character resides in the string
     * @param str string to search through
     * @param c character to find
     * @return times of occurences.
     */
    public static int numberOf(String str, char c) {
	    int count = 0;
	    for (int i = 0; i < str.length(); i++) {
	        if (str.charAt(i) == c) { count++; }
        }
        return count;
    }

    /**
     * Given a string, find the given character, at its
     * nth position.
     * @param str
     * @param ch
     * @param n
     * @return
     */
    public static int nthOccurrenceOf(String str, char ch, int n) {
    	for (int i = 0; i < str.length(); i++) {
    		if (str.charAt(i) != ch) { continue; }
    		n--;
    		if (n == 0) { return i; }
		}
		return -1;
	}
    /**
     * gets all of the elements of the character stack into a string
     * @param stack stack of characters
     * @return string that the character represents.
     */
    public static String extractStringFromStack(Stack<Character> stack) {
		StringBuilder sb = new StringBuilder();
		while (stack.isEmpty() == false) {
			sb.append(stack.pop());
		}
		return sb.toString();
	}
	public static String extractStringFromQueue(Queue<Character> queue) {
        if (queue.isEmpty()) { return ""; }
        StringBuilder sb = new StringBuilder();
        while (queue.isEmpty() == false) {
            sb.append(queue.poll());
        }
        return sb.toString();
    }
}
