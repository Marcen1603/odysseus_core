package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This class is used to represent objects as simple key value pairs
 * 
 * @author Marco Grawunder
 *
 * @param <T>
 */

public class KeyValueObject <T extends IMetaAttribute> extends AbstractStreamObject<T>
implements Serializable{
	
	private static final long serialVersionUID = -94667746890198612L;

	final private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public KeyValueObject(){
		
	}
	
	public KeyValueObject(KeyValueObject<T> other){
		super(other);
		this.attributes.putAll(other.attributes);	
	}
	
	//-----------------------------------------------
	// attribute methods
	// ----------------------------------------------
	
	@SuppressWarnings("unchecked")
	public final <K> K getAttribute(String key) {
		return (K) this.attributes.get(key);
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
			retBuff.append(";").append(getMetadata().csvToString());
		}
		return retBuff.toString();
	}

	// ------------------------------------
	
	@Override
	public KeyValueObject<T> clone() {
		return new KeyValueObject<T>(this);
	}

}
