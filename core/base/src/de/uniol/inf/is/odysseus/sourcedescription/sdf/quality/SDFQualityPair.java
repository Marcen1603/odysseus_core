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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.quality;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFQualityPair extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3225498771977738003L;

	/**
	 * @uml.property  name="qualityProperty"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFQuality qualityProperty = null;

    /**
	 * @uml.property  name="value"
	 */
    Object value = null;


	public SDFQualityPair(String URI, SDFQuality qualityProperty, Object value) {
		super(URI);
		this.qualityProperty = qualityProperty;
		this.value = value;
	}

    /**
     * 
     * @uml.property name="qualityProperty"
     */
    public void setQualityProperty(SDFQuality qualityProperty) {
        this.qualityProperty = qualityProperty;
    }

    /**
     * 
     * @uml.property name="qualityProperty"
     */
    public SDFQuality getQualityProperty() {
        return qualityProperty;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 
     * @uml.property name="value"
     */
    public Object getValue() {
        return value;
    }

}