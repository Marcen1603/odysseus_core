package de.uniol.inf.is.odysseus.wrapper.scai.physicaloperator.access;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;

public class SCAIHandler extends AbstractDataHandler<Object> {
	static protected List<String> types = new ArrayList<String>();

	@Override
	public Object readData(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object readData(ObjectInputStream inputStream)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object readData(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int memSize(Object attribute) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
