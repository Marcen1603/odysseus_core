package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.IClone;

public interface IObjectHandler<T> extends IClone {
	public void put(ByteBuffer buffer) throws IOException;
	public void put(ByteBuffer buffer, int size) throws IOException;
	public void put(T object);
	public ByteBuffer getByteBuffer();
	public T create() throws IOException, ClassNotFoundException;
}
