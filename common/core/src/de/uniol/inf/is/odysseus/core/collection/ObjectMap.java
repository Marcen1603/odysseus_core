package de.uniol.inf.is.odysseus.core.collection;

import java.util.HashMap;

import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil.ToStringAdapter;

import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Container class for simple object maps
 * 
 * @author Thomas Vogelgesang
 *
 * @param <T>
 */
public class ObjectMap<T extends IMetaAttribute> extends AbstractStreamObject<T> {

	private static final long serialVersionUID = 1L;

	// --------------------------------------------
	// Internal attributes
	// --------------------------------------------

	private HashMap<String, Object> data;

	// --------------------------------------------
	// Inherited Method implementation
	// --------------------------------------------

	@Override
	public String toString(boolean printMetadata) {
		StringBuilder sb = new StringBuilder();

		for (String str : data.keySet()) {
			sb.append("{Key: ").append(str).append(", Value: ").append(data.get(str).toString()).append("}");
		}

		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toString(false);
	}



	@Override
	public AbstractStreamObject<T> clone() {
		ObjectMap<T> clone = new ObjectMap<>();
		
		for (String key : this.data.keySet()) {
			clone.setAttribute(key, this.data.get(key));
		}
		
		return clone;
	}

	@Override
	public AbstractStreamObject<T> newInstance() {
		return new ObjectMap<T>();
	}

	// -------------------------------------------
	// Constructor(s)
	// -------------------------------------------

	public ObjectMap() {
		super();
		this.data = new HashMap<String, Object>();
	}

	// -------------------------------------------
	// Public methods
	// -------------------------------------------

	/**
	 * Gets the object stored under the given key.
	 * 
	 * @param key
	 *            the identifying key
	 * @return the stored value or null if the key does not exist
	 */
	public Object getValue(String key) {
		return this.data.get(key);
	}

	/**
	 * Adds the given object to the map for the given key. Updates the stored
	 * object for the key, if the key is already used in the object map.
	 * 
	 * @param key
	 *            the identifying key
	 * @param value
	 *            the object to add
	 */
	public void setAttribute(String key, Object value) {
		this.data.put(key, value);
	}
}
