package de.uniol.inf.is.odysseus.core.collection;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NestedKeyValueObject<T extends IMetaAttribute> extends KeyValueObject<T> {

	private static final long serialVersionUID = 3646987825952423104L;
	
	public NestedKeyValueObject(NestedKeyValueObject<T> nestedKeyValueObject) {
		super(nestedKeyValueObject);
	}
	
	public NestedKeyValueObject(Map<String, Object> map) {
		super(map);
	}

	@Override
	public NestedKeyValueObject<T> clone() {
		return new NestedKeyValueObject<T>(this);
	}

	@SuppressWarnings("unchecked")
	public <K> K getAttribute(String key) {
		try{
			String[] path = key.split("\\.");
			Map<String, Object> map = this.attributes;
			for(int i = 0; i < path.length - 1; i++) {
				map = (Map<String, Object>) map.get(path[i]);
			}
			K attribute = (K) map.get(path[path.length - 1]);
			if(attribute instanceof Map) {
				LOG.debug("Resolved attribute in KVO is map and can't be handled");
				return null;
			} else {
				return attribute;
			}
		} catch(Exception e) {
			LOG.debug(e.getMessage());
			return null;
		}
	}

}
