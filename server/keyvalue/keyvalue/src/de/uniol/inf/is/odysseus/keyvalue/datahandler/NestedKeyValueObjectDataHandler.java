package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import de.uniol.inf.is.odysseus.core.collection.NestedKeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Jan Sören Schwarz
 * 
 */
public class NestedKeyValueObjectDataHandler extends AbstractKeyValueObjectDataHandler<NestedKeyValueObject<?>> {

	protected static List<String> types = new ArrayList<String>();
	protected static final Logger LOG = LoggerFactory.getLogger(NestedKeyValueObjectDataHandler.class);
	
	static {
		types.add(SDFDatatype.NESTEDKEYVALUEOBJECT.getURI());
	}

	@Override
	public List<String> getSupportedDataTypes() {
		return types;
	}
	
	@Override
	public IDataHandler<NestedKeyValueObject<?>> getInstance(SDFSchema schema) {
		return new NestedKeyValueObjectDataHandler();
	}

	@Override
	public Class<?> createsType() {
		return NestedKeyValueObject.class;
	}
	
	@SuppressWarnings("unchecked")
	protected NestedKeyValueObject<?> jsonStringToKVO(String json) {
		try {
			LOG.debug("JSON-String: " + json);
			JsonNode rootNode = jsonMapper.reader().readTree(json);		
			if(!rootNode.isObject()) {
				//könnte das wirklich vorkommen?
				rootNode = rootNode.get(0);
			} 
			Map<String, Object> map = jsonMapper.reader().treeToValue(rootNode, Map.class);
			return new NestedKeyValueObject<>(map);
		} catch (IOException e) {
			e.printStackTrace();
			LOG.debug(e.getMessage());
			return null;
		}
	}
}
