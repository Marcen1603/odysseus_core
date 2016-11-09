package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

/**
 *
 * @author Jan-Sören Schwarz
 * @author Marco Grawunder
 *
 */

public class KeyValueObjectDataHandler extends AbstractKeyValueObjectDataHandler<KeyValueObject<?>> {

	protected static List<String> types = new ArrayList<String>();
	protected static final Logger LOG = LoggerFactory.getLogger(KeyValueObjectDataHandler.class);

	static {
		types.add(SDFDatatype.KEYVALUEOBJECT.getURI());
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}

	@Override
	public IDataHandler<KeyValueObject<?>> getInstance(SDFSchema schema) {
		return new KeyValueObjectDataHandler();
	}

	@Override
	public Class<?> createsType() {
		return KeyValueObject.class;
	}

}
