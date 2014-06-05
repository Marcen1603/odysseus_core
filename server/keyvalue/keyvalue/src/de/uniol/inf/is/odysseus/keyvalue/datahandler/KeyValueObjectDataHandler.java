package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

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
	
	protected KeyValueObject<?> jsonStringToKVO(String json) {
		try {
//			LOG.debug("JSON-String: " + json);
			if(json.equals("")) {
				throw new Exception("empty JSON-String");
			}
			JsonNode rootNode = jsonMapper.reader().readTree(json);		
			if(!rootNode.isObject()) {
				//könnte das wirklich vorkommen?
				rootNode = rootNode.get(0);
			} 
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			parse(rootNode, map, "");
			return new KeyValueObject<>(map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug(e.getMessage());
			return null;
		}
	}
	
	private void parse(JsonNode rootNode, Map<String, Object> map, String path) {
		Iterator<Entry<String, JsonNode>> nodeIterator = rootNode.fields();
		while(nodeIterator.hasNext()) {
			Entry<String, JsonNode> nodeEntry = nodeIterator.next();
			JsonNode node = nodeEntry.getValue();
			String key = nodeEntry.getKey();
			String newPath;
			if(path.equals("")) {
				newPath = key;
			} else {
				newPath = path + "." + key;				
			}
			if(node.size() > 0) {
				parse(node, map, newPath);
			} else {
				if(node.isInt()) {
					map.put(newPath, node.asInt());
				} else if(node.isTextual()) {
					map.put(newPath, node.asText());
				} else if(node.isBoolean()) {
					map.put(newPath, node.asBoolean());
				} else if(node.isDouble()) {
					map.put(newPath, node.asDouble());
				} else if(node.isLong()) {
					map.put(newPath, node.asLong());
				}
			}
		}
	}
}
