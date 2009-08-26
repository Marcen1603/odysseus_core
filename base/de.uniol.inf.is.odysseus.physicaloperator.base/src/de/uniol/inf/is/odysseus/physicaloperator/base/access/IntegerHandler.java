package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class IntegerHandler extends AbstractAtomicDataHandler {

	@Override
	public Object readData() throws IOException {
		return getStream().readInt();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		int i = buffer.getInt();
		//System.out.println("read Int Data: "+i);
		return i;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Integer Data "+(Integer)data);
		buffer.putInt(((Number) data).intValue());		
	}

	

}
