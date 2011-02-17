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
package de.uniol.inf.is.odysseus.logicaloperator.objectrelational.objecttracking;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListMetadataTypes;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * ObjectTrackingUnnestAO
 * 
 * @author Jendrik Poloczek
 */
public class ObjectTrackingUnnestAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private SDFAttributeListExtended outputSchema;
	private SDFAttribute nestAttribute;

	public ObjectTrackingUnnestAO() {
		super();
	}
	
	public ObjectTrackingUnnestAO(ObjectTrackingUnnestAO ao) {
		super(ao);
		this.outputSchema = ao.getOutputSchema();
		this.nestAttribute = ao.getNestAttribute();
	}
	
	@Override
	public ObjectTrackingUnnestAO clone() {
		return new ObjectTrackingUnnestAO(this);
	}
	
	@Override
	public SDFAttributeListExtended getOutputSchema() {
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
	
	private SDFAttributeListExtended calcOutputSchema(
		SDFAttributeList sdfAttributeList
	) {
	    SDFAttributeListExtended outputSchema;
	    SDFAttributeList subAttributes; 	   
	    
	    outputSchema = new SDFAttributeListExtended();
	    subAttributes = this.nestAttribute.getSubattributes();
	    	    
	    for(SDFAttribute attribute : sdfAttributeList) {
	        if(!attribute.equals(this.nestAttribute)) {
	            outputSchema.add(attribute);
	        }
	    }
	    
		outputSchema.addAll(subAttributes);
		
	    /*
	     * For the use of SDFAttributeListExtended for various 
	     * ObjectTracking operators prediction functions have to be
	     * specified as metadata in the schema. For evaluating 
	     * purpose prediction functions are empty.
	     */
	    	    
	    outputSchema.setMetadata(
	    	SDFAttributeListMetadataTypes.PREDICTION_FUNCTIONS, 
	    	new HashMap<IPredicate, IPredictionFunction>()
    	);
		
		return outputSchema;
	}
	
	public SDFAttribute getNestAttribute() {
		return this.nestAttribute;
	}
}
