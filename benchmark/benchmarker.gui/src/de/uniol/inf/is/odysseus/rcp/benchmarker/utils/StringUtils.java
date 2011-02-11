package de.uniol.inf.is.odysseus.rcp.benchmarker.utils;

/**
 * Diese Klasse überprüft, ob Strings empty oder blank sind
 * 
 * @author Stefanie Witzke
 * 
 */
public final class StringUtils {

	private StringUtils() {
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean isBlank(String str) {
		if (isEmpty(str)) {
			return true;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isNotBlank(String... strings) {
		if (strings == null) {
			return false;
		}
		for (String str : strings) {
			if (isNotBlank(str)) {
				return true;
			}
		}
		return false;
	}
}