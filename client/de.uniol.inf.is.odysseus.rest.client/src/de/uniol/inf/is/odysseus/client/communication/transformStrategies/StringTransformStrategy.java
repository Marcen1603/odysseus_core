package de.uniol.inf.is.odysseus.client.communication.transformStrategies;


import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thore Stratmann
 */
public class StringTransformStrategy implements ITransformStrategy{
	
	@Override
	public Object transformBytesToObject(DataInputStream inputStream) throws IOException {
		int length = inputStream.readInt();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i<length; i++) {
			builder.append((char) inputStream.readByte());

		}				
		return builder.toString();
	}
}
