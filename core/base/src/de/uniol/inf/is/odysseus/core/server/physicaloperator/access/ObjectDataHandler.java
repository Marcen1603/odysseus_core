package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectDataHandler extends AbstractDataHandler {

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("Object");
	}
	
	@Override
	public Object readData(ByteBuffer buffer) {
		return null;
	}

	@Override
	public Object readData(ObjectInputStream inputStream) throws IOException {
		try {
			return inputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object readData(String string) {
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
	}

	@Override
	public int memSize(Object attribute) {
		return 0;
	}

	@Override
	public Object readData() throws IOException {
		return null;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
