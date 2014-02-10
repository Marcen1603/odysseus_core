package de.uniol.inf.is.odysseus.wrapper.nmea.util;

import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.wrapper.nmea.sentence.Sentence;

public class SentenceUtils {
	private static final Pattern regExpChecksum = Pattern
			.compile("^[$|!]{1}[A-Z0-9]{3,10}[,][\\x20-\\x7F]*[*][A-F0-9]{2}$");

	private static final Pattern regExpNoChecksum = Pattern
			.compile("^[$|!]{1}[A-Z0-9]{3,10}[,][\\x20-\\x7F]*$");

	private SentenceUtils() {
	}

	public static boolean isSentence(String nmea) {
		if (nmea == null || "".equals(nmea)) {
			return false;
		}
		if (nmea.indexOf(Sentence.CHECKSUM_DELIMITER) < 0) {
			return regExpNoChecksum.matcher(nmea).matches();
		}
		return regExpChecksum.matcher(nmea).matches();
	}

	public static boolean validateSentence(String nmea) {
		if (SentenceUtils.isSentence(nmea)) {
			int i = nmea.indexOf(Sentence.CHECKSUM_DELIMITER);
			if (i > 0) {
				String sum = nmea.substring(++i, nmea.length());
				return sum.equals(calculateChecksum(nmea));
			} else {
				return true;
			}
		}
		return false;
	}
	
	public static String calculateChecksum(String nmea) {
		char ch;
		int sum = 0;
		for (int i = 0; i < nmea.length(); i++) {
			ch = nmea.charAt(i);
			if (i == 0
					&& (ch == Sentence.BEGIN_CHAR || ch == Sentence.ALTERNATIVE_BEGIN_CHAR)) {
				continue;
			} else if (ch == Sentence.CHECKSUM_DELIMITER) {
				break;
			} else if (sum == 0) {
				sum = (byte) ch;
			} else {
				sum ^= (byte) ch;
			}
		}
		return String.format("%02X", sum);
	}
	
	public static String getSentenceId(String nmea) {
		if (!isSentence(nmea)) {
			throw new IllegalArgumentException("String is not a sentence");
		}
		if (nmea.startsWith("$P")) {
			return nmea.substring(2, nmea.indexOf(','));
		} else {
			return nmea.substring(3, nmea.indexOf(','));
		}
	}
	
	public static String getTalkerId(String nmea) {
		if (!isSentence(nmea)) {
			throw new IllegalArgumentException("String is not a sentence");
		}

		if (nmea.startsWith("$P")) {
			return "P";
		} else {
			return nmea.substring(1, 3);
		}
	}
}
