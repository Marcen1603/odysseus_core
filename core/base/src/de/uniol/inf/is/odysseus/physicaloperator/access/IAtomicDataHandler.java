package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public interface IAtomicDataHandler {
	public Object readData() throws IOException;
	public Object readData(ByteBuffer buffer);
	public void writeData(ByteBuffer buffer, Object data);
	public void setStream(ObjectInputStream stream);
}
