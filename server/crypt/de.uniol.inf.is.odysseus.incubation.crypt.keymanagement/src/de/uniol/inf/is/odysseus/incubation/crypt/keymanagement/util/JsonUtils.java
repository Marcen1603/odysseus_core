/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import com.google.gson.Gson;

/**
 * @author PGSESAdata
 *
 */
public class JsonUtils {
	// TODO nice to have: es muesste in extra package das was auf dem server
	// laufen soll und das was lokal lauft
	/**
	 * Creates a json string representing a String array holding two values: 1.
	 * The simple name of the class of message. 2. The serialized form of
	 * message.
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
