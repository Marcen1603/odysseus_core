package de.uniol.inf.is.odysseus.keyvalue.datahandler;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	static private KeyValueObject<?> jsonStringToKVOWrapper(String json){
		try {
//			LOG.debug("JSON-String: " + json);
			if(json.equals("")) {
				throw new Exception("empty JSON-String");
			}
			JsonNode rootNode = jsonMapper.reader().readTree(json);		
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			if(rootNode.isArray()) {
				for(int i = 0; i < rootNode.size(); ++i) {
					parse(rootNode.get(i), map, "" + i);
				}
			} else {
				parse(rootNode, map, "");
			}
			return new KeyValueObject<>(map);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.debug(e.getMessage());
			return null;
		}
	}
	
	protected KeyValueObject<?> jsonStringToKVO(String json) {
		return jsonStringToKVOWrapper(json);
	}
	
	static private void parse(JsonNode rootNode, Map<String, Object> map, String path) {
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
			if(node.isArray()) {
				map.putAll(parseArray(node, map, newPath));				
			} else {
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

	static private Map<String, Object> parseArray(JsonNode rootNode, Map<String, Object> map, String path) {
		Map<String,Object> resultList = new HashMap<String,Object>();
		Iterator<JsonNode> elements = rootNode.elements();
		int pos = 0;
		while(elements.hasNext()) {
			JsonNode node = elements.next();
			String key = path+"["+pos+"]";
			if(node.isInt()) {
				resultList.put(key,node.asInt());
			} else if(node.isTextual()) {
				resultList.put(key,node.asText());
			} else if(node.isBoolean()) {
				resultList.put(key,node.asBoolean());
			} else if(node.isDouble()) {
				resultList.put(key,node.asDouble());
			} else if(node.isLong()) {
				resultList.put(key,node.asLong());
			} else if(node.isObject()){
				Map<String,Object> subMap = new HashMap<String, Object>();
				parse(node, subMap, key);
				resultList.putAll(subMap);
			}
			pos++;
		}
		return resultList;
	}
	
	

	public static void main(String[] args) {
		String jsonString = "{\"ok\":true,\"result\":[{\"update_id\":102358350,\"message\":{\"message_id\":9,\"from\":{\"id\":103101429,\"first_name\":\"Marco\",\"last_name\":\"Grawunder\",\"username\":\"MarcoGrawunder\"},\"chat\":{\"id\":103101429,\"first_name\":\"Marco\",\"last_name\":\"Grawunder\",\"username\":\"MarcoGrawunder\",\"type\":\"private\"},\"date\":1463736160,\"text\":\"Hallo Bot\"}},{\"update_id\":102358351, \"message\":{\"message_id\":11,\"from\":{\"id\":155288651,\"first_name\":\"Michael\",\"last_name\":\"Brand\",\"username\":\"MichaelBrand\"},\"chat\":{\"id\":155288651,\"first_name\":\"Michael\",\"last_name\":\"Brand\",\"username\":\"MichaelBrand\",\"type\":\"private\"},\"date\":1463736668,\"text\":\"Marco ist doch nicht so toll2\"}}]}";
		KeyValueObject<?> out = jsonStringToKVOWrapper(jsonString);
		System.out.println(out);
	}
	
}
