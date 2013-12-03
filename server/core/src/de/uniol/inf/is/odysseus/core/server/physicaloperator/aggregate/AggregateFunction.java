/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.IClone;

public class AggregateFunction implements
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
	public String toString() {
		return getName();
	}
	
	@Override
	public AggregateFunction clone(){
		return new AggregateFunction(this);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregateFunction other = (AggregateFunction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
