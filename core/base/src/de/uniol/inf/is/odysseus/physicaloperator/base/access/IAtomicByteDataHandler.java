package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public interface IAtomicByteDataHandler {
	public Object readData() throws IOException;
	public Object readData(ByteBuffer buffer);
	public void writeData(ByteBuffer buffer, Object data);
	public void setStream(BufferedInputStream stream);
}
