/** Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.service.sensor.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a tuple.
 */
public class DataTuple {
	
	/** The attributes for this tuple. */
	protected Map<String, Object> attributes;	
	
	/** The memory size in bytes. */
	protected int memSize = -1;
	
	/**
	 * Instantiates a new data tuple.
	 */
	public DataTuple(){
		this.attributes = new HashMap<String, Object>(); 
	}
	
	
	/**
	 * Instantiates a new data tuple from a copy.
	 *
	 * @param copy the copy
	 */
	public DataTuple(DataTuple copy) {				
		this.attributes = new HashMap<String,Object>(copy.getAttributes());		
	}	

	/**
	 * Gets an attribute.
	 *
	 * @param name the name
	 * @return the attribute or null if it not exists
	 */
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}
		
	/**
	 * Sets the value for an attribute.
	 *
	 * @param name the name of the attribute
	 * @param value the value for the attribute
	 */
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}	
	
	/**
	 * Gets all attributes.
	 *
	 * @return all attributes
	 */
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	/**
	 * Adds a new attribute.
	 *
	 * @param name the name of the attribute
	 * @param value the value for the attribute
	 */
	public void addAttribute(String name, Object value){		
		this.attributes.put(name, value);
	}
	
	/**
	 * Adds a collection of attributes.
	 * Passes each entry to addAttribute.
	 * The String is the name of the attribute and
	 * the Object is the value for the attribute.
	 *
	 * @param values the values
	 */
	public void addAttributes(Map<String, Object> values){
		for(Entry<String, Object> entry : values.entrySet()){
			this.addAttribute(entry.getKey(), entry.getValue());
		}
	}
	

	/**
	 * Calculates the memory size in bytes for this tuple.
	 *
	 * @param calcNew if true, then recalculate the memory size
	 * @return the size in bytes
	 */
	public int memSize(boolean calcNew) {
		if (memSize == -1 || calcNew) {
			memSize = calcSize();
		}
		return memSize;
	}

	/**
	 * Calculates the memory for a base type in bytes.
	 * Currently Integer, Double, Long and String are provided.
	 * For Integer, Double and Long the default bit-representation
	 * from java divided by 8 is used.
	 * Due to the problem that a String is not limited to the 
	 * length, it is represented by 2 parts. The first part is 
	 * an integer that holds the total length of the string. 
	 * The second part holds the string as a unicode-representation.
	 *
	 * @param attObject the att object
	 * @return the int
	 */
	private int calcBaseTypeSize(Object attObject) {
		if (attObject == null)
			return 0;
		if (attObject instanceof Integer)
			return Integer.SIZE / 8;
		if (attObject instanceof Double)
			return Double.SIZE / 8;
		if (attObject instanceof Long)
			return Long.SIZE / 8;
		if (attObject instanceof String)
			return ((String) attObject).length() * 2 // Unicode!
					+ Integer.SIZE / 8; // Für die Längeninformation										
		if (attObject instanceof DataTuple)
			return ((DataTuple) attObject).memSize(true);

		throw new IllegalArgumentException("Illegal Relation Attribute Type " + attObject);

	}

	/**
	 * Calculates the size for this tuple.
	 *
	 * @return the size
	 */
	private int calcSize() {
		int size = 0;
		for (Object attObject : attributes.values()) {
			size = size + calcSize(attObject);
		}
		return size;
	}
	
	/**
	 * Calculates the size for an object.
	 *
	 * @param attObject the object
	 * @return the size
	 */
	private int calcSize(Object attObject) {
		int size = 0;
		if (attObject instanceof Collection) {
			for (Object e : ((Collection<?>) attObject)) {
				size += calcSize(e);
			}

		} else {
			size += calcBaseTypeSize(attObject);
		}
		return size;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		int ret = 0;
		for (Object o : this.attributes.values()) {
			ret += o.hashCode();
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String out="";
		String sep="";
		for(Entry<String, Object> e : this.attributes.entrySet()){
			out=out+" "+sep+" "+e.getKey()+": "+e.getValue();
			sep = "|";
		}
		return out;
	}	
}
