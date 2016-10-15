package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import com.google.gson.Gson;

/**
 * Class with some util methods for JSON. Copied from PGSESAdata.
 * 
 * @author PGSESAdata
 *
 */
public class JsonUtils {

	/**
	 * Creates a json string representing a String array holding two values:
	 * <br>
	 * 1. The simple name of the class of message. <br>
	 * 2. The serialized form of message.
	 * 
	 * @param message
	 *            the message to be parsed to json.
	 * @return the String representing the message
	 */
	public static String getJsonString(Object message) {
		Gson gson = new Gson();
		String serialized = gson.toJson(message);
		return gson.toJson(new String[] { message.getClass().getSimpleName(), serialized });
	}

}
