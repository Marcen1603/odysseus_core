package de.uniol.inf.is.odysseus.core.datahandler;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.ObjectMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;


/**
 * This class is a dummy implementation which allows tuples to contain object
 * maps as attributes. This data handler is not intended to handle incoming
 * data.
 * 
 * @author Thomas Vogelgesang
 *
 */
public class ObjectMapDataHandler extends AbstractDataHandler<ObjectMap<IMetaAttribute>> {

	private static ArrayList<String> SUPPORTED_DATA_TYPES;
	static {
		SUPPORTED_DATA_TYPES = new ArrayList<>();
		SUPPORTED_DATA_TYPES.add(SDFDatatype.OBJECT_MAP.getURI());
	}

	@Override
	public ObjectMap<IMetaAttribute> readData(ByteBuffer buffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void writeData(ByteBuffer buffer, Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ObjectMap<IMetaAttribute> readData(String string) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int memSize(Object attribute) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> createsType() {
		return ObjectMapDataHandler.class;
	}

	@Override
	protected IDataHandler<ObjectMap<IMetaAttribute>> getInstance(SDFSchema schema) {
		return new ObjectMapDataHandler();
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return SUPPORTED_DATA_TYPES;
	}

}
