package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

public class StringHandler extends AbstractAtomicDataHandler {

	@Override
	final public Object readData() throws IOException {
		return getStream().readUTF();
	}

	@Override
	public Object readData(ByteBuffer b) {
		StringBuffer buf = new StringBuffer();
		int size = b.getInt();
		//System.out.println("size "+size);
		for (int i=0;i<size;i++){
			char c = b.getChar();
			//System.out.print(c);
			buf.append(c);
		}
		//System.out.println();
		return buf.toString();
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		String s = (String) data;
		//System.out.println("write String Data "+s+" "+s.length());		
		buffer.putInt(s.length());
		for (int i=0;i<s.length();i++){
			buffer.putChar(s.charAt(i));
		}
	}
}
