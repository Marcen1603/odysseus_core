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

		for (Entry<Integer, Map<String, Object>> e : sepObjects.entrySet()) {
			e.getValue().putAll(remaing);
			@SuppressWarnings("unchecked")
			T kv = (T) object.newInstance();
			kv.setMetadata(object.getMetadata().clone());
			transfer((T) kv);
		}

	}

}
