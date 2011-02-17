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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.function;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFFunction extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7664982077640439530L;
	/**
	 * @uml.property  name="reverseFunction"
	 * @uml.associationEnd  
	 */
    SDFFunction reverseFunction = null;


	public SDFFunction(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="reverseFunction"
     */
    public void setReverseFunction(SDFFunction reverseFunction) {
        this.reverseFunction = reverseFunction;
    }

    /**
     * 
     * @uml.property name="reverseFunction"
     */
    public SDFFunction getReverseFunction() {
        return reverseFunction;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + "\n");
		// Achtung Gefahr von Endlosschleifen, wenn hier nicht getURI verwendet
		// wird!
		if (this.getReverseFunction() != null)
			ret.append("Umkehrfunktion "
					+ this.getReverseFunction().getURI(true));
		return ret.toString();
	}
}