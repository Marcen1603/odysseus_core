package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This class is used to represent objects as simple key value pairs
 * 
 * @author Marco Grawunder, Jan Soeren Schwarz
 *
 * @param <T>
 */

public class KeyValueObject <T extends IMetaAttribute> extends AbstractStreamObject<T> implements Serializable{
	
	final private static long serialVersionUID = -94667746890198612L;

	final protected Map<String, Object> attributes = new HashMap<String, Object>();

	final protected static Logger LOG = LoggerFactory.getLogger(KeyValueObject.class);
	
	public KeyValueObject(){
	}
	
	public KeyValueObject(KeyValueObject<T> other){
		super(other);
		this.attributes.putAll(other.attributes);	
	}
	
	public KeyValueObject(Map<String,Object> map) {
		if(map != null && map.size() > 0) {
			this.attributes.putAll(map);
		}
	}
	
	//-----------------------------------------------
	// attribute methods
	// ----------------------------------------------

	@SuppressWarnings("unchecked")
	public <K> K getAttribute(String key) {
		return (K) this.attributes.get(key);
	}
	
	public final Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@SuppressWarnings("unchecked")
	public final void addAttributeValue(String key, Object value) {
		if (this.attributes.get(key) != null
				&& !(this.attributes.get(key) instanceof Collection)) {
			throw new RuntimeException(
					"Cannot add value to non collection type");
		}
		if (this.attributes.get(key) == null) {
			this.attributes.put(key, new ArrayList<Object>());
		}
		((Collection<Object>) this.attributes.get(key)).add(value);
	}

	public final void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}
	
	public final void setAttributes(List<String> keys, List<Object> values){
		if (keys.size() != values.size()){
			throw new IllegalArgumentException("Lists need to have the same length!");
		}
		for (int i=0;i<keys.size();i++){
			this.attributes.put(keys.get(i),values.get(i));
		}
	}

	public final void setAttributes(String[] keys, Object[] values){
		if (keys.length != values.length){
			throw new IllegalArgumentException("Lists need to have the same length!");
		}
		for (int i=0;i<keys.length;i++){
			this.attributes.put(keys[i],values[i]);
		}
	}

	
	public final int size() {
		return this.attributes.size();
	}
	
	
	@Override
	protected IStreamObject<T> process_merge(IStreamObject<T> left,
			IStreamObject<T> right, Order order) {
		KeyValueObject<T> merged = new KeyValueObject<T>((KeyValueObject<T>) left);
		merged.attributes.putAll(((KeyValueObject<T>)right).attributes);
		return merged;
	}
	
	public boolean isEmpty() {
		if(attributes.size() <= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// ---------------------------------
	// output methods
	// ---------------------------------

	@Override
	public String toString() {
		return toString(true);
	}
	
	public String toString(boolean withMetadata) {
		StringBuffer retBuff = new StringBuffer();
		retBuff.append(attributes);
		if (withMetadata && getMetadata() != null) {
			retBuff.append(";").append(getMetadata().csvToString(';','\'',null, null, true));
		}
		return retBuff.toString();
	}

	public String toStringWithNewlines(){
		StringBuffer retBuff = new StringBuffer();
		for(Entry<String, Object> e : attributes.entrySet()){
			retBuff.append(e.getKey()+" = "+e.getValue());
			retBuff.append(System.lineSeparator());			
		}
		return retBuff.toString();
	}
	// ------------------------------------
	
	@Override
	public KeyValueObject<T> clone() {
		return new KeyValueObject<T>(this);
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractStreamObject<T> newInstance() {
        return new KeyValueObject<>();
    }

	@SuppressWarnings("unchecked")
	public Map<String, Object> getAttributesAsNestedMap() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> tmpMap;
		Set<Entry<String, Object>> entrySet = this.attributes.entrySet();		
		for(Entry<String, Object> entry: entrySet) {
			String[] path = entry.getKey().split("\\.");
			tmpMap = map;
			for(int i = 0; i < path.length - 1; i++) {
				if(tmpMap.get(path[i]) != null) {
					tmpMap = (Map<String, Object>) tmpMap.get(path[i]);
				} else {
					Map<String, Object> newMap = new LinkedHashMap<String, Object>();
					tmpMap.put(path[i], newMap);
					tmpMap = newMap;
				}
			}
			tmpMap.put(path[path.length - 1], entry.getValue());
		}
		return map;
	}
	
	@Override
	public final boolean equals(Object o) {
		if (!(o instanceof KeyValueObject)) {
			return false;
		}
		KeyValueObject<?> t = (KeyValueObject<?>) o;
		if (this.attributes.size() != t.attributes.size()) {
			return false;
		}
		Iterator<String> it = this.attributes.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			// test if attributes are not null and equal
			// or both null (order is imporantant!)
			if (this.attributes.get(key) != null) {
				if (!this.attributes.get(key).equals(t.attributes.get(key))) {
					return false;
				}
			} else {
				if (t.attributes.get(key) != null) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Like normal equals-method but has a tolerance for double and float
	 * comparisons.
	 * 
	 * @param o
	 * @return
	 */
	@Override
	public final boolean equalsTolerance(Object o, double tolerance) {
		if (!(o instanceof KeyValueObject)) {
			return false;
		}
		KeyValueObject<?> t = (KeyValueObject<?>) o;
		if (this.attributes.size() != t.attributes.size()) {
			return false;
		}
		Iterator<String> it = this.attributes.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			Object attr = this.attributes.get(key);
			Object theirAttr = t.attributes.get(key);
			// test if attributes are not null and equal
			// or both null (order is imporantant!)
			if (attr != null) {
				if (attr instanceof Double && theirAttr instanceof Double) {
					if (Math.abs((Double) attr - (Double) theirAttr) > tolerance) {
						return false;
					}
				} else if (attr instanceof Float && theirAttr instanceof Float) {
					if (Math.abs((Float) attr - (Float) theirAttr) > tolerance) {
						return false;
					}
				} else {
					if (!attr.equals(theirAttr)) {
						return false;
					}
				}
			} else {
				if (theirAttr != null) {
					return false;
				}
			}
		}
		return true;
	}
}
