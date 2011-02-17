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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * @author  Marco Grawunder
 */
public class SDFAttributeAttributeBindingPair extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8332356474402713433L;

	/**
	 * @uml.property  name="attribute"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFAttribute attribute = null;

    /**
	 * @uml.property  name="attributeBinding"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    SDFAttributeBindung attributeBinding = null;


	public SDFAttributeAttributeBindingPair(String URI, SDFAttribute attribute,
			SDFAttributeBindung attributeBinding) {
		super(URI);
		this.attribute = attribute;
		this.attributeBinding = attributeBinding;
	}

	@Override
	public String toString() {
		//StringBuffer ret = new StringBuffer("(" + this.attribute.getURI(true));
		StringBuffer ret = new StringBuffer("(" + this.attribute);
		if (this.attributeBinding != null)
			ret.append("," + this.attributeBinding.toString());
		return ret.toString() + ")";
	}

    /**
     * 
     * @uml.property name="attribute"
     */
    public SDFAttribute getAttribute() {
        return attribute;
    }

    /**
     * 
     * @uml.property name="attributeBinding"
     */
    public SDFAttributeBindung getAttributeBinding() {
        return attributeBinding;
    }

    /**
	 * @param  newAttributeBinding
	 * @uml.property  name="attributeBinding"
	 */
    public void setAttributeBinding(SDFAttributeBindung attributeBinding) {
        this.attributeBinding = attributeBinding;
    }

}