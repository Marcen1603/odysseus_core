/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.physicaloperator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.IClone;

public class AggregateFunction implements Comparable<AggregateFunction>,
		Serializable, IClone {

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

	public AggregateFunction(AggregateFunction aggregateFunction) {
		this.name = aggregateFunction.name;
		this.properties = new HashMap<String, String>(aggregateFunction.properties);
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
	
	@Override
	public String toString() {
		return getName();
	}
	
	public AggregateFunction clone(){
		return new AggregateFunction(this);
	}

}
