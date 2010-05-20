package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class IntegerByteHandler extends AbstractAtomicByteDataHandler {

	byte data[];
	byte buff[];

	@Override
	final public Object readData() throws IOException {

		char c;
		String out;

		// would have liked to use readUTF, but it didn't seem to work
		// when talking to the c++ server

		out = new String("");

		while ((c = (char) this.getStream().read()) != '\n')
			out = out + String.valueOf(c);

		c = (char) this.getStream().read();
		return Integer.parseInt(out);
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		double d = buffer.getInt();
		// System.out.println("read Double Data: "+d);
		return d;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// System.out.println("write Double Data "+(Double)data);
		buffer.putInt(((Number) data).intValue());
	}
}
