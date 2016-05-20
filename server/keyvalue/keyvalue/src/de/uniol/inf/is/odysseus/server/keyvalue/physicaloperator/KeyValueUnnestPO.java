package de.uniol.inf.is.odysseus.server.keyvalue.physicaloperator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class KeyValueUnnestPO<T extends KeyValueObject<?>> extends AbstractPipe<T, T> {

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
		Map<String, Object> attributes = object.getAttributes();

		// find each attribute starting with attribute
		// ok=true,
		// result[1].message.chat.last_name=Brand,
		// result[0].message.chat.first_name=Marco,
		// result[0].message.chat.username=MarcoGrawunder,
		// result[0].message.text=Hallo Bot,
		// result[0].message.from.username=MarcoGrawunder,
		// result[0].message.date=1463736160,
		// result[1].message.chat.type=private,
		// result[0].message.from.last_name=Grawunder,
		// result[0].message.chat.last_name=Grawunder,
		// result[0].message.chat.type=private,
		// result[0].update_id=102358350,
		// result[0].message.from.id=103101429,
		// result[1].message.chat.id=155288651,
		// result[1].message.message_id=11,
		// result[0].message.chat.id=103101429,
		// result[1].message.from.last_name=Brand,
		// result[0].message.from.first_name=Marco,
		// result[1].message.chat.username=MichaelBrand,
		// result[1].message.from.id=155288651,
		// result[1].message.date=1463736668,
		// result[1].update_id=102358351,
		// result[1].message.text=Marco ist doch nicht so toll2,
		// result[1].message.from.username=MichaelBrand,
		// result[0].message.message_id=9,
		// result[1].message.from.first_name=Michael,
		// result[1].message.chat.first_name=Michael

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
			KeyValueObject<IMetaAttribute> kv = new KeyValueObject<>(e.getValue());
			kv.setMetadata(object.getMetadata().clone());
			transfer((T) kv);
		}

	}

}
