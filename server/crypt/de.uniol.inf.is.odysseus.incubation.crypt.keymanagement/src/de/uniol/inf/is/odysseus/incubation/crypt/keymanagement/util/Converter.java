/**
 * 
 */
package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;

/**
 * @author MarkMilster
 *
 */
public class Converter {
	
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
