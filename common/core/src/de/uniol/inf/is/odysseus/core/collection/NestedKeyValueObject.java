package de.uniol.inf.is.odysseus.core.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public class NestedKeyValueObject<T extends IMetaAttribute> extends KeyValueObject<T> {

	private static final long serialVersionUID = 3646987825952423104L;
	
	public NestedKeyValueObject() {
		super();
	}
	
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

	@Override
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

	/**
	 * Adds the given value to a existing collection identified by the key.
	 * If there is no collection an exception is raised.
	 * 
	 * @param key
	 * @param value
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addAttributeValue(String key, Object value) {
		String[] path = key.split("\\.");
		Map<String, Object> map = this.attributes;
		for(int i = 0; i < path.length - 1; i++) {
			map = (Map<String, Object>) map.get(path[i]);
		}		
		
		String leafKey = path[path.length - 1];
		Object oldAttribute = (Object) map.get(leafKey);
		if (oldAttribute != null && !(oldAttribute instanceof Collection)) {
			throw new RuntimeException("Cannot add value to non collection type");
		}
		
		if (oldAttribute == null) {
			map.put(leafKey, new ArrayList<Object>());
		}
		((Collection<Object>) map.get(leafKey)).add(value);
	}

	/**
	 * Set the value to given key. If there had been a value already, it will be overridden.
	 * 
	 * @param key
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setAttribute(String key, Object value) {
		String[] path = key.split("\\.");
		Map<String, Object> map = this.attributes;
		for(int i = 0; i < path.length - 1; i++) {
			if(map.get(path[i]) != null) {
				map = (Map<String, Object>) map.get(path[i]);
			} else {
				Map<String, Object> newMap = new HashMap<String, Object>();
				map.put(path[i], newMap);
				map = newMap;
			}
		}
		map.put(path[path.length - 1], value);
	}

	@Override
	public void setAttributes(List<String> keys, List<Object> values){
		if (keys.size() != values.size()){
			throw new IllegalArgumentException("Lists need to have the same length!");
		}
		for (int i=0;i<keys.size();i++){
			this.setAttribute(keys.get(i), values.get(i));
		}
	}

	@Override
	public void setAttributes(String[] keys, Object[] values){
		if (keys.length != values.length){
			throw new IllegalArgumentException("Lists need to have the same length!");
		}
		for (int i=0;i<keys.length;i++){
			this.setAttribute(keys[i],values[i]);
		}
	}

	@Override
	public Object removeAttribute(String key) {
		String[] path = key.split("\\.");
		try {
			return this.recursiveRemoveAttribute(path, this.attributes);
		} catch(Exception e) {
			LOG.debug("Attribute " + key + " could not be removed from NestedKeyValueObject.");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private Object recursiveRemoveAttribute(String[] path, Map<String, Object> map) throws NullPointerException {
		if(path.length > 2) {
			Map<String, Object> newMap = (Map<String, Object>) map.get(path[0]);
			String[] newPath = Arrays.copyOfRange(path, 1, path.length);
			return this.recursiveRemoveAttribute(newPath, newMap);
		} else if(path.length == 2) {
			Map<String, Object> newMap = (Map<String, Object>) map.get(path[0]);
			Object retObj = newMap.remove(path[1]);
			if(newMap.size() < 1) {
				map.remove(path[0]);
			}
			return retObj;
		} else {
			return this.attributes.remove(path[0]);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int size() {
		int size = 0;
		for(Entry<String, Object> att : this.attributes.entrySet()) {
			if(att.getValue() instanceof Map) {
				size += this.recursiveSize((Map<String, Object>) att.getValue());
			} else {
				size++;
			}
		}
		return size;
	}
	
	@SuppressWarnings("unchecked")
	private int recursiveSize(Map<String, Object> map) {
		int size = 0;
		for(Entry<String, Object> att : map.entrySet()) {
			if(att.getValue() instanceof Map) {
				size += this.recursiveSize((Map<String, Object>) att.getValue());
			} else {
				size++;
			}
		}
		return size;
	}
}
