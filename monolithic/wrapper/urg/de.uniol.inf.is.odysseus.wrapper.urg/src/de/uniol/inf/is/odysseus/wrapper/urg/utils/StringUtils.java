package de.uniol.inf.is.odysseus.wrapper.urg.utils;

import static de.uniol.inf.is.odysseus.wrapper.urg.utils.Constants.MAX_MESSAGE_LENGTH;

import java.util.regex.Pattern;

public class StringUtils {
	public static final String REGEX = "[a-zA-Z0-9\\_\\.\\+\\@\\ ]+";

	public static void validateString(String message) {
		if (message.length() > MAX_MESSAGE_LENGTH) {
			throw new UnsupportedCommandException(
					"Message exceeds maximum length of " + MAX_MESSAGE_LENGTH
							+ " characters.");
		}

		if (!Pattern.matches(REGEX, message)) {
			throw new UnsupportedCommandException(
					"Message has illegal characters. You only may use english letters, numbers, blank space and symbols '.', '_', '+', '@'.");
		}
	}
}
