package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class KeyValueUnnestPO<T extends KeyValueObject<IMetaAttribute>> extends AbstractPipe<T, T> {

	private String attribute;

	public KeyValueUnnestPO(String attribute) {
		this.attribute = attribute;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(T object, int port) {
		Map<String, Object> attributes = object.getAsKeyValueMap();

		// find each attribute starting with attribute

		Map<Integer, Map<String, Object>> sepObjects = new TreeMap<>();
		Map<String, Object> remaing = new HashMap<>();
		for (Entry<String, Object> v : attributes.entrySet()) {
			if (v.getKey().startsWith(attribute)) {
				// determine position information from string
				int pos = v.getKey().indexOf("[", attribute.length());
				int pos2 = v.getKey().indexOf("]", attribute.length());
				if (pos >= 0 && pos2 > 0) {
					int objectPos = Integer.parseInt(v.getKey().substring(pos + 1, pos2));
					Map<String, Object> subMap = sepObjects.get(objectPos);
					if (subMap == null) {
						subMap = new HashMap<>();
						sepObjects.put(objectPos, subMap);
					}
					String newPathName = v.getKey().substring(0, pos) + "." + v.getKey().substring(pos2+2); // remove ] and .
					subMap.put(newPathName, v.getValue());
				}
			}else{
				remaing.put(v.getKey(), v.getValue());
			}
		}

		createAndSendSeparatedObjects(object, sepObjects, remaing);
	}

	private void createAndSendSeparatedObjects(T originalKeyValueObject, Map<Integer, Map<String, Object>> seperatedObjects,
			Map<String, Object> remainingObjects) {
		for (Entry<Integer, Map<String, Object>> entry : seperatedObjects.entrySet()) {
			entry.getValue().putAll(remainingObjects);
			Map<String, Object> contentForKeyValueObject = entry.getValue();
			T keyValueObject = createAndFillKeyValueObject(contentForKeyValueObject, originalKeyValueObject);
			transfer((T) keyValueObject);
		}
	}
	
	private T createAndFillKeyValueObject(Map<String, Object> contentForKeyValueObject, T originalKeyValueObject) {
		@SuppressWarnings("unchecked")
		T keyValueObject = (T) originalKeyValueObject.newInstance();
		keyValueObject.setMetadata(originalKeyValueObject.getMetadata().clone());
		// Copy content into new keyValue stream object
		copyContentIntoKeyValueObject(contentForKeyValueObject, keyValueObject);
		return keyValueObject;
	}

	private void copyContentIntoKeyValueObject(Map<String, Object> contentForkeyValueObject, T keyValueObject) {
		for (String key : contentForkeyValueObject.keySet()) {
			  String unnestedKey = key.substring(attribute.length() + 1, key.length());
			  keyValueObject.setAttribute(unnestedKey, contentForkeyValueObject.get(key));
		}
	}
	
	

}
