package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * If you have a key value object that contains a list under a key, you can
 * unnest this list. Example: {"records":[
 * {"i":1000,"voltage":-174.27526077500005,"current":-0.1395625},
 * {"i":1001,"voltage":-176.040243225,"current":-0.6631625}, ... ]}
 * 
 * will be
 * 
 * {"current":-0.1395625,"i":1000,"voltage":-174.27526077500005};1581354685820|oo
 * {"current":-0.6631625,"i":1001,"voltage":-176.040243225};1581354685820|oo
 * 
 * @author created by ???, refactored and fixed by Tobias Brandt
 *
 */
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
		Map<Integer, Map<String, Object>> separatedKeyValuePairs = new TreeMap<>();
		Map<String, Object> remainingKeyValuePairs = new HashMap<>();

		// find each attribute starting with attribute
		for (Entry<String, Object> keyValuePair : attributes.entrySet()) {
			if (keyValuePair.getKey().startsWith(attribute)) {
				// determine position information from string
				int indexOpenBracket = keyValuePair.getKey().indexOf("[", attribute.length());
				int indexCloseBracket = keyValuePair.getKey().indexOf("]", attribute.length());
				// create a map for each index
				if (areValidIndexBracketPositions(indexOpenBracket, indexCloseBracket)) {
					putKVPairInSubMap(separatedKeyValuePairs, keyValuePair, indexOpenBracket, indexCloseBracket);
				}
			} else {
				remainingKeyValuePairs.put(keyValuePair.getKey(), keyValuePair.getValue());
			}
		}
		createAndSendSeparatedObjects(object, separatedKeyValuePairs, remainingKeyValuePairs);
	}

	private void putKVPairInSubMap(Map<Integer, Map<String, Object>> separatedObjects,
			Entry<String, Object> keyValuePair, int indexOpenBracket, int indexCloseBracket) {
		int objectPos = parseObjectIndexFromKey(keyValuePair.getKey(), indexOpenBracket, indexCloseBracket);
		Map<String, Object> subMap = getSubMapForCurrentKeyValueSubObject(separatedObjects, objectPos);
		// remove ] and .
		String newPathName = keyValuePair.getKey().substring(0, indexOpenBracket) + "."
				+ keyValuePair.getKey().substring(indexCloseBracket + 2);
		subMap.put(newPathName, keyValuePair.getValue());
	}

	private int parseObjectIndexFromKey(String key, int indexOpenBracket, int indexCloseBracket) {
		return Integer.parseInt(key.substring(indexOpenBracket + 1, indexCloseBracket));
	}

	private boolean areValidIndexBracketPositions(int indexOpenBracket, int indexCloseBracket) {
		return indexOpenBracket >= 0 && indexCloseBracket > 0;
	}

	private Map<String, Object> getSubMapForCurrentKeyValueSubObject(Map<Integer, Map<String, Object>> separatedObjects,
			int objectPos) {
		Map<String, Object> subMap = separatedObjects.get(objectPos);
		if (subMap == null) {
			subMap = new HashMap<>();
			separatedObjects.put(objectPos, subMap);
		}
		return subMap;
	}

	private void createAndSendSeparatedObjects(T originalKeyValueObject,
			Map<Integer, Map<String, Object>> seperatedObjects, Map<String, Object> remainingObjects) {
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
