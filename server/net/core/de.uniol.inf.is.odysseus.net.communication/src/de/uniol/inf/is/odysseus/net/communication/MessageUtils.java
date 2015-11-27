package de.uniol.inf.is.odysseus.net.communication;

import java.nio.ByteBuffer;

public class MessageUtils {

	public static void putString( ByteBuffer bb, String text ) {
		int textLength = text.length();
		bb.putInt(textLength);
		bb.put(text.getBytes());
	}
	
	public static String getString( ByteBuffer bb ) {
		int textLength = bb.getInt();
		byte[] bytes = new byte[textLength];
		bb.get(bytes);
		return new String(bytes);
	}
}
