package de.uniol.inf.is.odysseus.iec61970;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.base.IClone;


public interface IDataAccessHandler<T> extends IClone {
		public void setObjSize(int size);
		public void put(ByteBuffer buffer) throws IOException;
		public void put(ByteBuffer buffer, int size) throws IOException;
		public void put(T object);
		public ByteBuffer getByteBuffer();
		public T create() throws IOException, ClassNotFoundException;
		public IDataAccessHandler<T> clone();
}
