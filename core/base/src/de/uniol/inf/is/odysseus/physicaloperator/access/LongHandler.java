package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class LongHandler extends AbstractAtomicDataHandler {

	@Override
	public final Object readData() throws IOException {
		return getStream().readLong();
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		long l = buffer.getLong();
		//System.out.println("read Long Data: "+l);
		return l;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		//System.out.println("write Long Data "+((Number)data).longValue());
		buffer.putLong(((Number)data).longValue());
	}
	
	

}
