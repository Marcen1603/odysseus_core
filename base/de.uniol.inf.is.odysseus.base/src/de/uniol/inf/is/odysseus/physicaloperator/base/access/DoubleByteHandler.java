package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DoubleByteHandler extends AbstractAtomicByteDataHandler {

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


		return Double.parseDouble(out);
		/*double receivedDouble;
		data = new byte[1280];
		buff = new byte[8];
		String dbl = "";
		
		int numbytes;
		int i;
		
		int totalbytes = 0;

		//while (totalbytes < 8) {
			numbytes = getStream().read(data);
			// copy the bytes into the result buffer
			for (i = totalbytes; i < 8; i++)
				dbl += data[i - totalbytes];

			totalbytes += numbytes;
		//}
		numbytes = getStream().read(data);
		// now we must convert bytes to double

		ByteArrayInputStream bytestream;
		DataInputStream instream;

		bytestream = new ByteArrayInputStream(data);
		instream = new DataInputStream(bytestream);

		receivedDouble = instream.readDouble();

		return receivedDouble;*/
	}

	@Override
	public Object readData(ByteBuffer buffer) {
		double d = buffer.getDouble();
		// System.out.println("read Double Data: "+d);
		return d;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// System.out.println("write Double Data "+(Double)data);
		buffer.putDouble(((Number) data).doubleValue());
	}
}
