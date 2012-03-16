package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.List;

public interface IDataHandler {
	public Object readData(ByteBuffer buffer);
	public Object readData(ObjectInputStream inputStream) throws IOException;
	public Object readData(String[] input);
	public Object readData(String string);
	public void writeData(ByteBuffer buffer, Object data);
	public List<String> getSupportedDataTypes();
	public int memSize(Object attribute);
	
	@Deprecated
	public Object readData() throws IOException;
	@Deprecated
	public void setStream(ObjectInputStream stream);

}
