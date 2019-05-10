package de.uniol.inf.is.odysseus.client.communication.transformStrategies;


import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author Thore Stratmann
 */
public class ByteTransformStrategy implements ITransformStrategy{

	@Override
	public Object transformBytesToObject(DataInputStream inputStream) throws IOException {
        return inputStream.readByte();
	}
}
