package de.uniol.inf.is.odysseus.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AggregateFunction implements Comparable<AggregateFunction>,
		Serializable {

	/**
* 
*/
	private static final long serialVersionUID = 8226289067622943217L;
	private final String name;
	private Map<String, String> properties = new HashMap<String, String>();

	public AggregateFunction(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * @param key
	 * @return the property
	 */
	public String getProperty(String key) {
		return properties.get(key);
	}

	/**
	 * @param key
	 * @param value
	 *            the property to set
	 */
	public void setProperties(String key, String value) {
		this.properties.put(key, value);
	}

	@Override
	public int compareTo(AggregateFunction o) {
		return this.name.compareToIgnoreCase(o.getName());
	}

}
