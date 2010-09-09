package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleHandler extends AbstractAtomicDataHandler {

	@Override
	final public Object readData() throws IOException {
		return getStream().readDouble();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		double d = buffer.getDouble();
		//System.out.println("read Double Data: "+d);
		return d;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Double Data "+(Double)data);
		buffer.putDouble(((Number)data).doubleValue());		
	}
	

}
