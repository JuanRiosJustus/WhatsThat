package utlility;

public class StringUtility {
	
	public static boolean isNumeric(String str) {
		if (str == null || str.length() < 1) { return false; }
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c) == false) { return false; }
		}
		return true;
	}
}
