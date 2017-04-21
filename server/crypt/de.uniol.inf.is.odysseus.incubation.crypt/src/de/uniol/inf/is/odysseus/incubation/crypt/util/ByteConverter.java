package de.uniol.inf.is.odysseus.incubation.crypt.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * This is a util class to convert byte streams
 * 
 * @author MarkMilster
 *
 */
public class ByteConverter {

	/**
	 * The given object will be parsed to an byteArray
	 * 
	 * @param obj
	 *            The Object to parse
	 * @return The byteArray that can be reparsed into the object
	 */
	public static byte[] objectToBytes(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bytes = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			bytes = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * Parse a byteArray to an object
	 * 
	 * @param bytes
	 *            The byteArray to parse
	 * @return The object out of the byteArray
	 */
	public static Object bytesToObject(byte[] bytes) {
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Object o = null;
		try {
			in = new ObjectInputStream(bis);
			o = in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException ex) {
				// ignore close exception
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}
		return o;
	}

	/**
	 * Parses a byteBuffer to a byteArray
	 * 
	 * @param byteBuffer
	 *            the byteBuffer to parse
	 * @return the byteArray, containing the bytes in the ByteBuffer
	 */
	public static byte[] byteBufferToBytes(ByteBuffer byteBuffer) {
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes, 0, bytes.length);
		return bytes;
	}

}
