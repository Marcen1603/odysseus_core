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
	
	public static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

}
