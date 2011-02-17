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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFSource extends SDFElement {
//
//	static public enum SourceType {
//		Relational, RelationalStreaming, RDFStreaming, RelationalInputStreamAccessPO, RelationalAtomicDataInputStreamAccessPO, RelationalByteBufferAccessPO,
//		RelationalAtomicDataInputStreamAccessMVPO, OSGI
//	}
//	
	private static final long serialVersionUID = 4621120226185967024L;

	/**
	 * TODO RDF Schema muss noch angepasst werden
	 */
	private String sourceType;

	private Map<String, String> optionalAttributes;

	/**
	 * @param URI
	 */
	public SDFSource(String URI, String sourceType) {
		super(URI);
		this.sourceType = sourceType;
		this.optionalAttributes = new HashMap<String, String>();
	}

	public void setOptionalAttribute(String name, String value) {
		this.optionalAttributes.put(name, value);
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public Integer getIntegerAttribut(String name) {
		return Integer.parseInt(this.optionalAttributes.get(name));
	}

	public String getStringAttribute(String name) {
		return this.optionalAttributes.get(name);
	}

	public Double getRealAttribute(String name) {
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

}