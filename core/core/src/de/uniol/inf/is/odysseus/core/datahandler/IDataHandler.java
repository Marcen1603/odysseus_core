package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IDataHandler<T> {
	public T readData(ByteBuffer buffer);
	public T readData(ObjectInputStream inputStream) throws IOException;
	public T readData(String[] input);
	public T readData(String string);
	public void writeData(ByteBuffer buffer, Object data);
	public List<String> getSupportedDataTypes();
	public int memSize(Object attribute);
	public IDataHandler<T> getInstance(SDFSchema schema);
	public IDataHandler<T> getInstance(List<String> schema);
	public void setPrototype(boolean b);
	public boolean isPrototype();
}
