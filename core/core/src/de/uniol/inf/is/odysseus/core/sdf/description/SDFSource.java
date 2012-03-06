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
package de.uniol.inf.is.odysseus.core.sdf.description;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.SDFElement;

/**
 * This class represents a source element.
 * 
 * @author Marco Grawunder
 *
 */

public class SDFSource extends SDFElement {
	
	private static final long serialVersionUID = 4621120226185967024L;

	/**
	 * Source type as name
	 */
	final private String sourceType;

	/**
	 * Additional information that can be added to the source
	 */
	final private Map<String, String> optionalAttributes;

	/**
	 * Create a new source with URI and source type
	 * @param URI
	 * @param sourceType
	 */
	public SDFSource(String URI, String sourceType) {
		super(URI);
		this.sourceType = sourceType;
		this.optionalAttributes = new HashMap<String, String>();
	}

	public SDFSource(String URI, String sourceType, Map<String, String> optionalAttributes ) {
		super(URI);
		this.sourceType = sourceType;
		this.optionalAttributes = new HashMap<String, String>(optionalAttributes);
	}

	
	/**
	 * Copy Constructor, needed by clone and child classes
	 * @param sdfSource
	 */
	protected SDFSource(SDFSource sdfSource) {
		super(sdfSource);
		this.sourceType = sdfSource.sourceType;
		this.optionalAttributes = sdfSource.optionalAttributes;
	}

	/**
	 * Returns the type of the source
	 * @return
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * Get the value of the optional attribute as Integer
	 * @param name
	 * @return
	 * @throws NumberFormatException if value cannot be parsed correctly
	 */
	public Integer getIntegerAttribut(String name) throws NumberFormatException {
		return Integer.parseInt(this.optionalAttributes.get(name));
	}

	/**
	 * Get the value of the optional attribute as String
	 * @param name
	 * @return
	 */
	public String getStringAttribute(String name) {
		return this.optionalAttributes.get(name);
	}

	/**
	 * Get the value of the optional attribute as Double
	 * @param name
	 * @return
	 * @throws NumberFormatException if value cannot be parsed correctly
	 */
	public Double getRealAttribute(String name) throws NumberFormatException{
		return Double.parseDouble(this.optionalAttributes.get(name));
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((optionalAttributes == null) ? 0 : optionalAttributes.hashCode());
		result = PRIME * result + ((sourceType == null) ? 0 : sourceType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
//		if (getClass() != obj.getClass())
//			return false;
		final SDFSource other = (SDFSource) obj;
		if (optionalAttributes == null) {
			if (other.optionalAttributes != null)
				return false;
		} else if (!optionalAttributes.equals(other.optionalAttributes))
			return false;
		if (sourceType == null) {
			if (other.sourceType != null)
				return false;
		} else if (!sourceType.equals(other.sourceType))
			return false;
		return true;
	}
	
	@Override
	public SDFSource clone() {
		return new SDFSource(this);
	}

}