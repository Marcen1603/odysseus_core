package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectDataHandler<M> extends AbstractDataHandler<M> {

	static protected List<String> types = new ArrayList<String>();
	static{
		types.add("Object");
	}
	
	@Override
	public M readData(ByteBuffer buffer) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public M readData(ObjectInputStream inputStream) throws IOException {
		try {
			return (M) inputStream.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public M readData(String string) {
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
	public M readData() throws IOException {
		return null;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

}
