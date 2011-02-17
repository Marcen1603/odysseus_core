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

public class SDFAccessPattern extends SDFElement {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3298970851864201598L;

	/**
	 * @uml.property  name="inputPatt"
	 * @uml.associationEnd  
	 */
    private SDFInputPattern inputPatt = null;

    /**
	 * @uml.property  name="outputPatt"
	 * @uml.associationEnd  
	 */
    private SDFOutputPattern outputPatt = null;


	public SDFAccessPattern(String URI) {
		super(URI);
	}

    /**
     * 
     * @uml.property name="inputPatt"
     */
    public void setInputPatt(SDFInputPattern inputPatt) {
        this.inputPatt = inputPatt;
    }

    /**
     * 
     * @uml.property name="inputPatt"
     */
    public SDFInputPattern getInputPatt() {
        return inputPatt;
    }

    /**
     * 
     * @uml.property name="outputPatt"
     */
    public void setOutputPatt(SDFOutputPattern outputPatt) {
        this.outputPatt = outputPatt;
    }

    /**
     * 
     * @uml.property name="outputPatt"
     */
    public SDFOutputPattern getOutputPatt() {
        return outputPatt;
    }

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer(super.toString() + ": {");
		if (this.inputPatt != null)
			ret.append(this.inputPatt.toString());
		ret.append(" --> ");
		if (this.outputPatt != null)
			ret.append(this.outputPatt.toString());
		return ret.toString() + "}";
	}

}