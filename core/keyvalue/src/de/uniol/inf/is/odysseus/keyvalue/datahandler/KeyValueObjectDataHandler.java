package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jan Sören Schwarz
 * 
 */
public class KeyValueObjectDataHandler extends AbstractDataHandler<KeyValueObject<?>> {

	static protected List<String> types = new ArrayList<String>();
	static {
		types.add(SDFDatatype.KEYVALUEOBJECT.getURI());
	}
	
	public KeyValueObjectDataHandler() {
	}
	
	@Override
	public IDataHandler<KeyValueObject<?>> getInstance(SDFSchema schema) {
		return new KeyValueObjectDataHandler();
	}
	
	@Override
	public KeyValueObject<?> readData(ByteBuffer buffer) {
		return null;
	}

	@Override
	public KeyValueObject<?> readData(ObjectInputStream inputStream)
			throws IOException {
		return null;
	}

	@Override
	public KeyValueObject<?> readData(String data) {
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
	public Class<?> createsType() {
		return KeyValueObject.class;
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
}
