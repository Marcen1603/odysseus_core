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
package de.uniol.inf.is.odysseus.logicaloperator.objectrelational;

import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * UnnestAO 
 * 
 * @author Jendrik Poloczek
 */
public class UnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeList outputSchema = null;
	private SDFAttribute nestAttribute = null;

	public UnnestAO() {
		super();
	}
	
	public UnnestAO(UnnestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.nestAttribute = ao.getNestAttribute();
	}
	
	@Override
	public UnnestAO clone() {
		return new UnnestAO(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		if(outputSchema == null) 
			this.outputSchema = calcOutputSchema(this.getInputSchema());
		return outputSchema;
	}

	public void setNestAttribute(SDFAttribute nestAttribute) {
		this.nestAttribute = nestAttribute.clone();
	}
	
	/**
	 * modify input schema, adding attributes of nesting, and 
	 * remove nesting attribute.
	 */	
	
	private SDFAttributeList calcOutputSchema(SDFAttributeList inputSchema) {
	    SDFAttributeList outputSchema;
	    SDFAttributeList subAttributes; 	   
	    
	    outputSchema = new SDFAttributeList();
	    subAttributes = this.nestAttribute.getSubattributes();
	    
	    for(SDFAttribute attribute : inputSchema) {
	        if(!attribute.equals(this.nestAttribute)) {
	            outputSchema.add(attribute);
	        }
	    }
	    
		outputSchema.addAll(subAttributes);
		return outputSchema;
	}
	
	public SDFAttribute getNestAttribute() {
		return nestAttribute;
	}
}
