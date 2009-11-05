package de.uniol.inf.is.odysseus.iec61970;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;




@SuppressWarnings("unchecked")
public class DataAccessHandler<T> implements IDataAccessHandler<T>{
	private byte[] receiveObj;
	private T object;
	private int objSize = -1 ;
	private int currentSize = 0;

	public DataAccessHandler() {
		super();
	}
	
public DataAccessHandler(DataAccessHandler<T> handler) {
	this.receiveObj = handler.receiveObj;
	this.object = handler.object;
	this.objSize = handler.objSize;
	this.currentSize = handler.currentSize;
}
@Override
public T create() throws IOException, ClassNotFoundException {
	ByteArrayInputStream bis = new ByteArrayInputStream(this.receiveObj); 
    ObjectInputStream ois = new ObjectInputStream(bis); 
    T data = null;
		data = (T) ois.readObject();
//		System.out.println("ID: "+data.getId().getContainer()+" "+data.getId().getFragment());
    ois.close(); 
    bis.close();
    this.receiveObj = null;
    this.currentSize = 0;
    this.objSize = -1;
    return data;
}

@Override
public ByteBuffer getByteBuffer() {
	return null;
}

@Override
public void put(ByteBuffer buffer) throws IOException {
		currentSize = currentSize + buffer.remaining();
		System.arraycopy(buffer.array(), buffer.position(), this.receiveObj, currentSize-buffer.remaining(), buffer.remaining());
		buffer.position(buffer.position()+buffer.remaining());
	
}

@Override
public void put(ByteBuffer buffer, int size) throws IOException {
	System.arraycopy(buffer.array(), buffer.position(), this.receiveObj, objSize-size, size);
	currentSize = currentSize + size;
	buffer.position(buffer.position()+size);
}

@Override
public void put(T object) {
	this.object = object;
}

@Override
public void setObjSize(int size) {
	this.objSize = size;
	this.receiveObj = new byte[objSize]; 
}

@Override
public IDataAccessHandler<T> clone() {
	return new DataAccessHandler(this);
	
}

}
