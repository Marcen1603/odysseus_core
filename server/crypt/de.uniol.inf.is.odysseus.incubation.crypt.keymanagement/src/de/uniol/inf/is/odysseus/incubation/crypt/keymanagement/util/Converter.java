package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;

/**
 * Converter for bytes and keys.
 * 
 * @author MarkMilster
 *
 */
public class Converter {

	/**
	 * Converts the given ByteArray to a Key with a ByteStream
	 * 
	 * @param bytes
	 *            ByteArray to parse
	 * @return The Key, parsed from the ByteArray, or null, if there was an
	 *         error while parsing.
	 */
	public static Key bytesToKey(byte[] bytes) {
		Key key = null;
		ByteArrayInputStream baip = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(baip);
			key = (Key) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return key;
	}

}
