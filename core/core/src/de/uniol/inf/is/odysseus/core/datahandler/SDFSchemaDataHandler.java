package de.uniol.inf.is.odysseus.core.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class SDFSchemaDataHandler extends AbstractDataHandler<SDFSchema> {

	@Override
	public SDFSchema readData(ByteBuffer buffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFSchema readData(ObjectInputStream inputStream) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDFSchema readData(String string) {
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
	protected IDataHandler<SDFSchema> getInstance(SDFSchema schema) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		// TODO Auto-generated method stub
		return null;
	}

}
